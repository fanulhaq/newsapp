/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.utils

import com.google.android.material.tabs.TabLayout

abstract class OnTabSelectedListener : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.let { onStateChanged(it) }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    abstract fun onStateChanged(tab: TabLayout.Tab)
}