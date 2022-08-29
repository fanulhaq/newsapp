/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.data.cache.room.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: List<SourceEntity>)

    @Query("DELETE FROM tbl_source WHERE category =:category")
    suspend fun deleteDataByCategory(category: String)

    @Query("SELECT * FROM tbl_source WHERE category =:category")
    fun getDataByCategory(category: String): Flow<List<SourceEntity>>

    @Query("DELETE FROM tbl_source")
    suspend fun deleteData()

    @Query("SELECT * FROM tbl_source")
    fun getData(): Flow<List<SourceEntity>>
}