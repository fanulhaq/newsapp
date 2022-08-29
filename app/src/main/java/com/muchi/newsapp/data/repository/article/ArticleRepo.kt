/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.muchi.newsapp.data.repository.article

import com.muchi.newsapp.data.cache.room.article.ArticleEntity
import com.muchi.newsapp.data.model.ArticleResponse
import com.muchi.newsapp.data.usecase.Resource
import kotlinx.coroutines.flow.Flow

interface ArticleRepo {
    suspend fun articleBySource(source: String) : Flow<Resource<List<ArticleEntity>>>
    suspend fun searchArticle(q: String) : Flow<Resource<ArticleResponse>>
}