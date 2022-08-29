/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.muchi.newsapp.data.repository.source

import android.content.Context
import com.muchi.newsapp.data.cache.room.source.SourceDao
import com.muchi.newsapp.data.cache.room.source.SourceEntity
import com.muchi.newsapp.data.model.SourceResponse
import com.muchi.newsapp.data.service.MainApi
import com.muchi.newsapp.data.usecase.Resource
import com.muchi.newsapp.data.usecase.ResourceBound
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SourceRepoImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mainApi: MainApi,
    private val sourceDao: SourceDao
) : SourceRepo {

    override suspend fun sources(category: String): Flow<Resource<List<SourceEntity>>> {
        return object : ResourceBound<List<SourceEntity>, SourceResponse>() {
            override suspend fun saveRemoteData(response: SourceResponse) {
                response.sources?.let {
                    sourceDao.insertData(it)
                }
            }
            override suspend fun fetchFromRemote(): Response<SourceResponse> = mainApi.sources(category)
            override suspend fun deleteData() = sourceDao.deleteDataByCategory(category)
            override fun fetchFromLocal(): Flow<List<SourceEntity>> = sourceDao.getDataByCategory(category)
        }.asFlow()
    }

    override suspend fun allSources(): Flow<Resource<List<SourceEntity>>> {
        return object : ResourceBound<List<SourceEntity>, SourceResponse>() {
            override suspend fun saveRemoteData(response: SourceResponse) {
                response.sources?.let {
                    sourceDao.insertData(it)
                }
            }
            override suspend fun fetchFromRemote(): Response<SourceResponse> = mainApi.allSources()
            override suspend fun deleteData() = sourceDao.deleteData()
            override fun fetchFromLocal(): Flow<List<SourceEntity>> = sourceDao.getData()
        }.asFlow()
    }
}