/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.di

import com.muchi.newsapp.data.repository.article.ArticleRepo
import com.muchi.newsapp.data.repository.article.ArticleRepoImpl
import com.muchi.newsapp.data.repository.source.SourceRepo
import com.muchi.newsapp.data.repository.source.SourceRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindSourceRepository(repository: SourceRepoImpl): SourceRepo

    @Binds
    abstract fun bindArticleRepository(repository: ArticleRepoImpl): ArticleRepo
}