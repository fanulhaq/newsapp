/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

@file:Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")

package com.muchi.newsapp.ui.main

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.muchi.newsapp.R
import com.muchi.newsapp.data.cache.prefs.UserStore
import com.muchi.newsapp.data.cache.room.source.SourceEntity
import com.muchi.newsapp.data.usecase.Resource
import com.muchi.newsapp.databinding.FragmentMainBinding
import com.muchi.newsapp.extension.safetyNavigate
import com.muchi.newsapp.ui.base.BaseFragment
import com.muchi.newsapp.ui.common.adapter.SourceAdapter
import com.muchi.newsapp.ui.common.adapter.SourceListener
import com.muchi.newsapp.ui.common.snackBarError
import com.muchi.newsapp.ui.search.DialogSearch.Companion.dialogSearch
import com.muchi.newsapp.ui.search.DialogSearch.Companion.setSearchCallback
import com.muchi.newsapp.ui.search.SearchCallback
import com.muchi.newsapp.utils.OnTabSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main), SourceListener,
    SearchCallback {

    private val viewModel: MainViewModel by viewModels()
    @Inject lateinit var userStore: UserStore
    @Inject lateinit var sourceAdapter: SourceAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager
    private var category: String? = null

    override fun initViews() = with(binding) {
        super.initViews()
        thisFragment = this@MainFragment

        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = sourceAdapter
        }
        sourceAdapter.listener = this@MainFragment

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    viewShadow.isVisible = true
                } else {
                    if(linearLayoutManager.findFirstVisibleItemPosition() == 0)
                        viewShadow.isVisible = false
                }
            }
        })
        viewShadow.isVisible = linearLayoutManager.findFirstVisibleItemPosition() != 0

        tabLayout.setOnTabSelectedListener(object : OnTabSelectedListener() {
            override fun onStateChanged(tab: TabLayout.Tab) {
                category = tab.text.toString().toLowerCase(Locale.getDefault())
                viewModel.sources("$category")
                setSelectedTabPosition(tab.position)
            }
        })

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            category?.let { viewModel.sources(it) }
        }

        setSearchCallback(this@MainFragment)
    }

    override fun subscribeToObservables() = with(binding) {
        super.subscribeToObservables()
        viewModel.category.observe(viewLifecycleOwner) { data ->
            if(tabLayout.tabCount == 0) {
                var position = -1
                runBlocking {
                    launch(Dispatchers.IO) {
                        position = userStore.tabPosition.first()
                    }
                }

                data.forEach {
                    tabLayout.addTab(tabLayout.newTab().setText(it))
                }

                if(position != -1 && position <= tabLayout.tabCount) {
                    tabLayout.getTabAt(position)?.select()
                }
            }
        }

        userStore.search.asLiveData().observe(viewLifecycleOwner) {
            search.setText(
                if(it == 0) R.string.search_news_sources else R.string.search_news_articles
            )
        }

        viewModel.sources.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    viewShadow.isVisible = false
                    swipeRefresh.isRefreshing = true
                    swipeRefresh.isEnabled = true
                    sourceAdapter.clearAdapter()
                    imageView.isVisible = false
                }
                is Resource.Success -> {
                    if(state.data.isEmpty()) {
                        if(state.finishLoading && sourceAdapter.items.isEmpty()) {
                            imageView.isVisible = true
                        }
                    } else {
                        imageView.isVisible = false
                        sourceAdapter.addAll(state.data as ArrayList<SourceEntity>, true)
                    }

                    if(state.finishLoading) {
                        swipeRefresh.isRefreshing = false
                        swipeRefresh.isEnabled = false
                    }
                }
                is Resource.Error -> {
                    swipeRefresh.isRefreshing = false
                    swipeRefresh.isEnabled = true
                    if(sourceAdapter.items.isEmpty()) imageView.isVisible = true
                    context?.snackBarError(layoutInflater = layoutInflater, v = root, message = state.message)
                }
            }
        }
    }

    fun toSearch() = dialogSearch(requireActivity().supportFragmentManager)

    override fun onSourceListener(data: SourceEntity) {
        findNavController().safetyNavigate(MainFragmentDirections.actionToArticle(data))
    }

    override fun onSearchCallback(event: Int, sourceData: SourceEntity, url: String) {
        when(event) {
            0 -> findNavController().safetyNavigate(MainFragmentDirections.actionToArticle(sourceData))
            1 -> findNavController().safetyNavigate(MainFragmentDirections.actionToWebview(url))
        }
    }

    private fun setSelectedTabPosition(position: Int) = lifecycleScope.launch(Dispatchers.IO) {
        userStore.tabPosition(position)
    }
}