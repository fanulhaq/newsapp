/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

@file:Suppress("DEPRECATION")

package com.muchi.newsapp.ui.search

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RadioButton
import android.widget.TextView.OnEditorActionListener
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.muchi.newsapp.R
import com.muchi.newsapp.data.cache.prefs.UserStore
import com.muchi.newsapp.data.cache.room.article.ArticleEntity
import com.muchi.newsapp.data.cache.room.source.SourceEntity
import com.muchi.newsapp.data.usecase.Resource
import com.muchi.newsapp.databinding.DialogSearchBinding
import com.muchi.newsapp.extension.formatterDate
import com.muchi.newsapp.ui.common.adapter.ArticleAdapter
import com.muchi.newsapp.ui.common.adapter.ArticleListener
import com.muchi.newsapp.ui.common.adapter.SourceAdapter
import com.muchi.newsapp.ui.common.adapter.SourceListener
import com.muchi.newsapp.ui.common.snackBarError
import com.muchi.newsapp.ui.common.snackBarWarning
import com.muchi.newsapp.utils.AfterTextChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DialogSearch : BottomSheetDialogFragment(), ArticleListener, SourceListener {

    private lateinit var binding: DialogSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    @Inject lateinit var userStore: UserStore
    @Inject lateinit var sourceAdapter: SourceAdapter
    @Inject lateinit var articleAdapter: ArticleAdapter

    private lateinit var behavior: BottomSheetBehavior<*>
    private var searchType = -1
    private var sourceData: ArrayList<SourceEntity> = ArrayList()

    companion object {
        fun dialogSearch(fm: FragmentManager) {
            val d = DialogSearch()
            val ft = fm.beginTransaction()
            val prev = fm.findFragmentByTag(DialogSearch::class.java.simpleName)
            if (prev != null) ft.remove(prev)
            d.show(ft, DialogSearch::class.java.simpleName)
        }

        private lateinit var callbackListener: SearchCallback
        fun setSearchCallback(listener: SearchCallback) {
            callbackListener = listener
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = super.onCreateDialog(savedInstanceState)
        binding = DialogSearchBinding.inflate(layoutInflater)

        try {
            dialogBinding.setContentView(binding.root)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

        initView()
        subscribeToObservables()
        return dialogBinding
    }

    private fun initView() = with(binding) {
        thisFragment = this@DialogSearch

        behavior = BottomSheetBehavior.from(root.parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
        behavior.isDraggable = false
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED

                if (newState == BottomSheetBehavior.STATE_HIDDEN)
                    dismiss()
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        search.addTextChangedListener(object : AfterTextChangeListener() {
            override fun onStateChanged(s: Editable) {
                icClear.isVisible = s.isNotEmpty()
                if(s.isEmpty()) {
                    when(searchType) {
                        0 -> sourceAdapter.clearAdapter()
                        1 -> articleAdapter.clearAdapter()
                    }
                } else {
                    getData()
                }
            }
        })

        search.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getData()
                return@OnEditorActionListener true
            }
            false
        })

        radioGroup.setOnCheckedChangeListener { v, checkedId ->
            val selectedRadioButton = v.findViewById<RadioButton>(checkedId)
            when(selectedRadioButton?.text.toString()) {
                resources.getString(R.string.source) -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        userStore.search(0)
                    }
                }
                resources.getString(R.string.article) -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        userStore.search(1)
                    }
                }
            }
        }

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            getData()
        }

        search.requestFocus()
        sourceAdapter.listener = this@DialogSearch
        articleAdapter.listener = this@DialogSearch
    }

    private fun subscribeToObservables() = with(binding) {
        userStore.search.asLiveData().observe(this@DialogSearch) {
            searchType = it

            if(it == 0) {
                search.setHint(R.string.search_news_sources)
                recyclerView.adapter = sourceAdapter
                if(!rbSource.isChecked && !rbArticle.isChecked) rbSource.isChecked = true
                if(viewModel.sources.value !is Resource.Success) viewModel.sources()
            } else {
                search.setHint(R.string.search_news_articles)
                recyclerView.adapter = articleAdapter
                if(!rbSource.isChecked && !rbArticle.isChecked) rbArticle.isChecked = true
            }

            search.setText("")
        }

        viewModel.sources.observe(this@DialogSearch) { state ->
            when (state) {
                is Resource.Loading -> {
                    swipeRefresh.isRefreshing = true
                    swipeRefresh.isEnabled = true
                    sourceAdapter.clearAdapter()
                }
                is Resource.Success -> {
                    if(state.data.isNullOrEmpty()) {
                        if(state.finishLoading) {
                            dialog?.window?.decorView?.let {
                                context?.snackBarWarning(
                                    layoutInflater = layoutInflater, v = it,
                                    message = "Data not found!", padding = resources.getDimensionPixelSize(R.dimen._50dp)
                                )
                            }
                        }
                    } else {
                        sourceData.clear()
                        sourceData.addAll(state.data)
                        if(!search.text.isNullOrEmpty()) searchSources()
                    }

                    if(state.finishLoading) {
                        swipeRefresh.isRefreshing = false
                        swipeRefresh.isEnabled = false
                    }
                }
                is Resource.Error -> {
                    swipeRefresh.isRefreshing = false
                    swipeRefresh.isEnabled = true
                    dialog?.window?.decorView?.let {
                        context?.snackBarError(
                            layoutInflater = layoutInflater, v = it,
                            message = state.message, padding = resources.getDimensionPixelSize(R.dimen._50dp)
                        )
                    }
                }
            }
        }

        viewModel.article.observe(this@DialogSearch) { state ->
            when (state) {
                is Resource.Loading -> {
                    swipeRefresh.isRefreshing = true
                    swipeRefresh.isEnabled = true
                    sourceAdapter.clearAdapter()
                }
                is Resource.Success -> {
                    if(state.data.articles.isNullOrEmpty()) {
                        dialog?.window?.decorView?.let {
                            context?.snackBarWarning(
                                layoutInflater = layoutInflater, v = it,
                                message = "Data not found!", padding = resources.getDimensionPixelSize(R.dimen._50dp)
                            )
                        }
                    } else {
                        val data: ArrayList<ArticleEntity> = ArrayList()
                        state.data.articles?.forEach {
                            data.add(
                                ArticleEntity(
                                    source = it.source?.id,
                                    author = it.author,
                                    title = it.title,
                                    url = it.url,
                                    urlToImage = it.urlToImage,
                                    publishedAt = it.publishedAt?.formatterDate()
                                )
                            )
                        }
                        articleAdapter.addAll(data)
                    }

                    if(state.finishLoading) {
                        swipeRefresh.isRefreshing = false
                        swipeRefresh.isEnabled = false
                    }
                }
                is Resource.Error -> {
                    swipeRefresh.isRefreshing = false
                    swipeRefresh.isEnabled = true
                    dialog?.window?.decorView?.let {
                        context?.snackBarError(
                            layoutInflater = layoutInflater, v = it,
                            message = state.message, padding = resources.getDimensionPixelSize(R.dimen._50dp)
                        )
                    }
                }
            }
        }
    }

    private fun getData() {
        when(searchType) {
            0 -> {
                if(sourceData.isNullOrEmpty()) viewModel.sources()
                else searchSources()
            }
            1 -> viewModel.article("${binding.search.text}")
        }
    }

    private fun searchSources() = lifecycleScope.launch(Dispatchers.Default) {
        if(!sourceData.isNullOrEmpty()) {
            val temp: ArrayList<SourceEntity> = ArrayList()
            sourceData.forEach { d ->
                if (d.name?.lowercase(Locale.getDefault())?.contains("${binding.search.text}".lowercase(Locale.getDefault())) == true)
                    temp.add(d)
            }

            launch(Dispatchers.Main) {
                if(temp.isNullOrEmpty()) {
                    dialog?.window?.decorView?.let {
                        context?.snackBarWarning(
                            layoutInflater = layoutInflater, v = it,
                            message = "Data not found!", padding = resources.getDimensionPixelSize(R.dimen._50dp)
                        )
                    }
                }
                sourceAdapter.addAll(temp, true)
            }
        }
    }

    fun onDismiss() = dialog?.dismiss()
    fun clearKeyword() = with(binding) {
        search.setText("")
    }

    override fun onArticleListener(data: ArticleEntity) {
        callbackListener.onSearchCallback(1, SourceEntity(), "${data.url}")
        dialog?.dismiss()
    }

    override fun onSourceListener(data: SourceEntity) {
        callbackListener.onSearchCallback(0, data, "")
        dialog?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}

interface SearchCallback {
    fun onSearchCallback(event: Int, sourceData: SourceEntity, url: String)
}