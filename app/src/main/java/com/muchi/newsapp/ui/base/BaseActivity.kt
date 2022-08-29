/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.muchi.newsapp.ui.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding>(@LayoutRes val layoutRes: Int) : AppCompatActivity() {

    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, layoutRes)
        initViews(savedInstanceState)
        subscribeToObservables()
    }

    @CallSuper
    protected open fun initViews(savedInstanceState: Bundle?) {

    }

    protected open fun subscribeToObservables() {

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}