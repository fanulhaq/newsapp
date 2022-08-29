/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.data.model

import com.muchi.newsapp.data.cache.room.source.SourceEntity

data class SourceResponse(
    var status: String? = null,
    var totalResults: String? = null,
    var code: String? = null,
    var message: String? = null,
    var sources: List<SourceEntity>? = null
)