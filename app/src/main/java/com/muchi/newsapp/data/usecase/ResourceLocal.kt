/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.data.usecase

import androidx.annotation.MainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
abstract class ResourceLocal<T> {

    fun asFlow() = flow {
        emit(Resource.loading())
        emitAll(
            fetchFromLocal().map {
                Resource.success(it, true)
            }
        )
    }.catch { e ->
        emit(Resource.error(e.message.toString(), -1))
        e.printStackTrace()
    }

    @MainThread
    protected abstract fun fetchFromLocal(): Flow<T>
}