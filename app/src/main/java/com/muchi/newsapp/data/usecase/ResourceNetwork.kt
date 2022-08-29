/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.data.usecase

import androidx.annotation.MainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.Response

@ExperimentalCoroutinesApi
abstract class ResourceNetwork<T>  {

    lateinit var result: Flow<T>

    fun asFlow() = flow {
        emit(Resource.loading())

        val apiResponse = fetchFromRemote()
        val remoteBody = apiResponse.body()

        if(apiResponse.isSuccessful && remoteBody != null) {
            result = setResult(remoteBody)
            emitAll(
                fetchFromData().map {
                    Resource.success(it, true)
                }
            )
        } else {
            emit(Resource.error(apiResponse.message(), apiResponse.code()))
        }
    }.catch { e ->
        emit(Resource.error(e.message.toString(), -1))
        e.printStackTrace()
    }

    @MainThread
    protected abstract fun fetchFromData(): Flow<T>

    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<T>

    private fun setResult(response: T): Flow<T> {
        return flow { emit(response) }
    }
}