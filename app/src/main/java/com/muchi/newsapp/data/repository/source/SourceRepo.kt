/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.muchi.newsapp.data.repository.source

import com.muchi.newsapp.data.cache.room.source.SourceEntity
import com.muchi.newsapp.data.usecase.Resource
import kotlinx.coroutines.flow.Flow

interface SourceRepo {
    suspend fun sources(category: String) : Flow<Resource<List<SourceEntity>>>
    suspend fun allSources() : Flow<Resource<List<SourceEntity>>>
}