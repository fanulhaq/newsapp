/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.muchi.newsapp.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muchi.newsapp.data.cache.room.article.ArticleEntity
import com.muchi.newsapp.data.repository.article.ArticleRepoImpl
import com.muchi.newsapp.data.usecase.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class ArticleViewModel @Inject constructor(
    private val articleRepo: ArticleRepoImpl
): ViewModel() {

    private var _articleBySource = MutableLiveData<Resource<List<ArticleEntity>>>()
    val articleBySource: LiveData<Resource<List<ArticleEntity>>>
        get() = _articleBySource

    fun articleBySource(source: String){
        viewModelScope.launch {
            articleRepo.articleBySource(source).collect {
                _articleBySource.postValue(it)
            }
        }
    }
}