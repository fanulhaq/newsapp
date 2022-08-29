/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.data.cache.room.article

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_article")
data class ArticleEntity(
    var source: String? = null,
    var author: String? = null,
    var title: String? = null,
    var url: String? = null,
    var urlToImage: String? = null,
    var publishedAt: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}