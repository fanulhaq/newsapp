/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.data.cache.room.article

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: List<ArticleEntity>)

    @Query("DELETE FROM tbl_article WHERE source =:source")
    suspend fun deleteData(source: String)

    @Query("SELECT * FROM tbl_article WHERE source =:source")
    fun getData(source: String): Flow<List<ArticleEntity>>
}