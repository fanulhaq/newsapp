/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.muchi.newsapp.ui.main

import androidx.lifecycle.*
import com.muchi.newsapp.data.cache.room.source.SourceEntity
import com.muchi.newsapp.data.repository.source.SourceRepoImpl
import com.muchi.newsapp.data.usecase.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val sourceRepo: SourceRepoImpl
): ViewModel() {

    private val _category = MutableLiveData<ArrayList<String>>()

    init {
        val categoryData = arrayListOf(
            "All",
            "Business",
            "Entertainment",
            "General",
            "Health",
            "Science",
            "Sports",
            "Technology"
        )
        category(categoryData)
    }

    val category: LiveData<ArrayList<String>>
        get() = _category

    fun category(value: ArrayList<String>){
        viewModelScope.launch {
            _category.postValue(value)
        }
    }

    private var _sources = MutableLiveData<Resource<List<SourceEntity>>>()
    val sources: LiveData<Resource<List<SourceEntity>>>
        get() = _sources

    private var sourcesJob: Job? = null

    fun sources(category: String){
        if(sourcesJob?.isActive == true) sourcesJob?.cancel()
        sourcesJob = viewModelScope.launch {
            if(category == "all") {
                sourceRepo.allSources().collect {
                    _sources.postValue(it)
                }
            } else {
                sourceRepo.sources(category).collect {
                    _sources.postValue(it)
                }
            }
        }
    }
}