/*
 * Copyright (c) 2022 - Muchi (Irfanul Haq).
 */

package com.muchi.newsapp.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewHolder<T>(
    containerView: View
) : RecyclerView.ViewHolder(containerView) {
    abstract fun bind(item: T)
}
