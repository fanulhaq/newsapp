/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.muchi.newsapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muchi.newsapp.data.cache.room.source.SourceEntity
import com.muchi.newsapp.data.model.ArticleResponse
import com.muchi.newsapp.data.repository.article.ArticleRepoImpl
import com.muchi.newsapp.data.repository.source.SourceRepoImpl
import com.muchi.newsapp.data.usecase.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class SearchViewModel @Inject constructor(
    private val sourceRepo: SourceRepoImpl,
    private val articleRepo: ArticleRepoImpl
): ViewModel() {

    private var searchJob: Job? = null

    private var _sources = MutableLiveData<Resource<List<SourceEntity>>>()
    val sources: LiveData<Resource<List<SourceEntity>>>
        get() = _sources

    fun sources(){
        if(searchJob?.isActive == true) searchJob?.cancel()
        searchJob = viewModelScope.launch {
            sourceRepo.allSources().collect {
                _sources.postValue(it)
            }
        }
    }

    private var _article = MutableLiveData<Resource<ArticleResponse>>()
    val article: LiveData<Resource<ArticleResponse>>
        get() = _article

    fun article(q: String){
        if(searchJob?.isActive == true) searchJob?.cancel()
        searchJob = viewModelScope.launch {
            articleRepo.searchArticle(q).collect {
                _article.postValue(it)
            }
        }
    }
}