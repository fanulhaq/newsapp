/*
 * Copyright (c) 2021. ~ Irfanul Haq.
 */

package com.muchi.newsapp.data.model

import com.muchi.newsapp.data.cache.room.source.SourceEntity

data class ArticleResponse(
    var status: String? = null,
    var totalResults: String? = null,
    var code: String? = null,
    var message: String? = null,
    var articles: List<ArticleModel>? = null
)

data class ArticleModel(
    var source: SourceEntity? = null,
    var author: String? = null,
    var title: String? = null,
    var url: String? = null,
    var urlToImage: String? = null,
    var publishedAt: String? = null
)