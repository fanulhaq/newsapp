/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.ui.main

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.muchi.newsapp.R
import com.muchi.newsapp.databinding.ActivityMainBinding
import com.muchi.newsapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
    }
}