/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

@file:Suppress("DEPRECATION")

package com.muchi.newsapp.ui.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.muchi.newsapp.data.cache.room.article.ArticleEntity
import com.muchi.newsapp.databinding.ItemArticleBinding
import com.muchi.newsapp.extension.loadImageUrl
import com.muchi.newsapp.ui.base.BaseRecyclerViewAdapter
import com.muchi.newsapp.ui.base.BaseRecyclerViewHolder
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject


class ArticleAdapter @Inject constructor(
    @ActivityContext private val context: Context
) : BaseRecyclerViewAdapter<ArticleEntity, ArticleAdapter.ViewHolder>() {

    var listener: ArticleListener? = null

    override fun createItemViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun bindItemViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(
        private val binding: ItemArticleBinding
    ) : BaseRecyclerViewHolder<ArticleEntity>(binding.root)  {

        override fun bind(item: ArticleEntity) {
            with(binding) {
                when(position){
                    0 -> {
                        lastView.isVisible = false
                        firstView.isVisible = true
                    }
                    items.lastIndex -> {
                        lastView.isVisible = true
                        firstView.isVisible = false
                    }
                    else -> {
                        lastView.isVisible = false
                        firstView.isVisible = false
                    }
                }

                context.loadImageUrl(imageView, item.urlToImage.toString())
                title.text = item.title
                author.text = item.author ?: "-"
                dateTime.text = item.publishedAt

                layContent.setOnClickListener {
                    listener?.onArticleListener(item)
                }
            }
        }
    }
}

interface ArticleListener {
    fun onArticleListener(data: ArticleEntity)
}