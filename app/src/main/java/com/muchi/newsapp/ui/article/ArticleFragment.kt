/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.muchi.newsapp.ui.article

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muchi.newsapp.R
import com.muchi.newsapp.data.cache.room.article.ArticleEntity
import com.muchi.newsapp.data.usecase.Resource
import com.muchi.newsapp.databinding.FragmentArticleBinding
import com.muchi.newsapp.extension.safetyNavigate
import com.muchi.newsapp.ui.base.BaseFragment
import com.muchi.newsapp.ui.common.adapter.ArticleAdapter
import com.muchi.newsapp.ui.common.adapter.ArticleListener
import com.muchi.newsapp.ui.common.snackBarError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ArticleFragment : BaseFragment<FragmentArticleBinding>(R.layout.fragment_article),
    ArticleListener {

    private val viewModel: ArticleViewModel by viewModels()
    private val args: ArticleFragmentArgs by navArgs()
    @Inject lateinit var articleAdapter: ArticleAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun initViews() = with(binding) {
        super.initViews()
        thisFragment = this@ArticleFragment
        title = args.data.name

        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.apply {
            adapter = articleAdapter
            layoutManager = linearLayoutManager
        }
        articleAdapter.listener = this@ArticleFragment

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    viewLine.isVisible = true
                } else {
                    if(linearLayoutManager.findFirstVisibleItemPosition() == 0)
                        viewLine.isVisible = false
                }
            }
        })
        viewLine.isVisible = linearLayoutManager.findFirstVisibleItemPosition() != 0

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            viewModel.articleBySource("${args.data.id}")
        }
    }

    override fun subscribeToObservables() = with(binding) {
        super.subscribeToObservables()
        viewModel.articleBySource.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    viewLine.isVisible = false
                    swipeRefresh.isRefreshing = true
                    swipeRefresh.isEnabled = true
                    imageView.isVisible = false
                }
                is Resource.Success -> {
                    if(state.data.isEmpty()) {
                        if(state.finishLoading && articleAdapter.items.isEmpty()) {
                            imageView.isVisible = true
                        }
                    } else {
                        imageView.isVisible = false
                        articleAdapter.addAll(state.data as ArrayList<ArticleEntity>, true)
                    }

                    if(state.finishLoading) {
                        swipeRefresh.isRefreshing = false
                        swipeRefresh.isEnabled = false
                    }
                }
                is Resource.Error -> {
                    swipeRefresh.isRefreshing = false
                    swipeRefresh.isEnabled = true
                    if(articleAdapter.items.isEmpty()) imageView.isVisible = true
                    context?.snackBarError(layoutInflater = layoutInflater, v = root, message = state.message)
                }
            }
        }

        if (viewModel.articleBySource.value !is Resource.Success) viewModel.articleBySource("${args.data.id}")
    }

    fun onBack() = findNavController().popBackStack()

    override fun onArticleListener(data: ArticleEntity) {
        findNavController().safetyNavigate(ArticleFragmentDirections.actionToWebview(data.url.toString()))
    }
}