/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.data.cache.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muchi.newsapp.data.cache.room.article.ArticleDao
import com.muchi.newsapp.data.cache.room.article.ArticleEntity
import com.muchi.newsapp.data.cache.room.source.SourceDao
import com.muchi.newsapp.data.cache.room.source.SourceEntity

@Database(
    entities = [
        ArticleEntity::class,
        SourceEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RoomDB: RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun sourceDao(): SourceDao
}