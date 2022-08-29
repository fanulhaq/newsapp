/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.muchi.newsapp.data.service

import com.muchi.newsapp.BuildConfig.API_KEY
import com.muchi.newsapp.data.model.ArticleResponse
import com.muchi.newsapp.data.model.SourceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MainApi {

    @GET("sources")
    suspend fun sources(@Query("category") category: String,
                           @Query("apiKey") apiKey: String = API_KEY): Response<SourceResponse>

    @GET("sources")
    suspend fun allSources(@Query("apiKey") apiKey: String = API_KEY): Response<SourceResponse>

    @GET("top-headlines")
    suspend fun articleBySource(@Query("sources") sources: String,
                                 @Query("apiKey") apiKey: String = API_KEY): Response<ArticleResponse>

    @GET("everything")
    suspend fun searchArticle(@Query("q") q: String,
                                 @Query("apiKey") apiKey: String = API_KEY): Response<ArticleResponse>
}