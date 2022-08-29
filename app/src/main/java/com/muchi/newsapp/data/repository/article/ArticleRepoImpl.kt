/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.muchi.newsapp.data.repository.article

import android.content.Context
import com.muchi.newsapp.data.cache.room.article.ArticleDao
import com.muchi.newsapp.data.cache.room.article.ArticleEntity
import com.muchi.newsapp.data.model.ArticleResponse
import com.muchi.newsapp.data.service.MainApi
import com.muchi.newsapp.data.usecase.Resource
import com.muchi.newsapp.data.usecase.ResourceBound
import com.muchi.newsapp.data.usecase.ResourceNetwork
import com.muchi.newsapp.extension.formatterDate
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ArticleRepoImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mainApi: MainApi,
    private val articleDao: ArticleDao
) : ArticleRepo {

    override suspend fun articleBySource(source: String): Flow<Resource<List<ArticleEntity>>> {
        return object : ResourceBound<List<ArticleEntity>, ArticleResponse>() {
            override suspend fun saveRemoteData(response: ArticleResponse) {
                val data: ArrayList<ArticleEntity> = ArrayList()
                response.articles?.forEach {
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
                articleDao.insertData(data)
            }
            override suspend fun fetchFromRemote(): Response<ArticleResponse> = mainApi.articleBySource(source)
            override suspend fun deleteData() = articleDao.deleteData(source)
            override fun fetchFromLocal(): Flow<List<ArticleEntity>> = articleDao.getData(source)
        }.asFlow()
    }

    override suspend fun searchArticle(q: String): Flow<Resource<ArticleResponse>> {
        return object : ResourceNetwork<ArticleResponse>() {
            override suspend fun fetchFromRemote(): Response<ArticleResponse> = mainApi.searchArticle(q)
            override fun fetchFromData(): Flow<ArticleResponse>  = result
        }.asFlow()
    }
}