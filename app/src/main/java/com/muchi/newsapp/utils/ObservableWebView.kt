/*
 * Copyright (c) 2021 - Irfanul Haq.
 */

package com.muchi.newsapp.utils

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView


class ObservableWebView : WebView {

    private var onScrollChanged: OnScrollChangedCallback? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (onScrollChanged != null) {
            onScrollChanged?.onScroll(l, t, oldl, oldt)
        }
    }

    fun getOnScrollChangedCallback(): OnScrollChangedCallback? {
        return onScrollChanged
    }

    fun setOnScrollChangedCallback(onScrollChangedCallback: OnScrollChangedCallback) {
        onScrollChanged = onScrollChangedCallback
    }

    interface OnScrollChangedCallback {
        fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int)
    }
}