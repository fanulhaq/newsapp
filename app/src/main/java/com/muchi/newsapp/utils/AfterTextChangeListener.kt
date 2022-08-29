/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.muchi.newsapp.utils

import android.text.Editable
import android.text.TextWatcher

abstract class AfterTextChangeListener : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        s?.let { onStateChanged(it) }
    }

    abstract fun onStateChanged(s: Editable)
}