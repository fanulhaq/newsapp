/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.di

import android.app.Application
import androidx.room.Room
import com.muchi.newsapp.data.cache.room.RoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(application: Application) = Room.databaseBuilder(
        application,
        RoomDB::class.java,
        "db_newsapp"
    )
    .fallbackToDestructiveMigration()
    .build()

    @Singleton
    @Provides
    fun provideSourceDao(db: RoomDB) = db.sourceDao()

    @Singleton
    @Provides
    fun provideArticleDao(db: RoomDB) = db.articleDao()
}