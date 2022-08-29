/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

@file:Suppress("DEPRECATION")

package com.muchi.newsapp.ui.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.muchi.newsapp.data.cache.room.source.SourceEntity
import com.muchi.newsapp.databinding.ItemSourceBinding
import com.muchi.newsapp.ui.base.BaseRecyclerViewAdapter
import com.muchi.newsapp.ui.base.BaseRecyclerViewHolder
import dagger.hilt.android.qualifiers.ActivityContext
import java.util.*
import javax.inject.Inject


class SourceAdapter @Inject constructor(
    @ActivityContext private val context: Context
) : BaseRecyclerViewAdapter<SourceEntity, SourceAdapter.ViewHolder>() {

    var listener: SourceListener? = null

    override fun createItemViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun bindItemViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(
        private val binding: ItemSourceBinding
    ) : BaseRecyclerViewHolder<SourceEntity>(binding.root)  {

        override fun bind(item: SourceEntity) {
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

                name.text = item.name
                category.text = item.category?.capitalize(Locale.getDefault())
                desc.text = item.description

                layContent.setOnClickListener {
                    listener?.onSourceListener(item)
                }
            }
        }
    }
}

interface SourceListener {
    fun onSourceListener(data: SourceEntity)
}