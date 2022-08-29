/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

@file:SuppressLint("SetJavaScriptEnabled")
@file:Suppress("OVERRIDE_DEPRECATION")

package com.muchi.newsapp.ui.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.muchi.newsapp.R
import com.muchi.newsapp.databinding.FragmentWebviewBinding
import com.muchi.newsapp.ui.base.BaseFragment
import com.muchi.newsapp.utils.ObservableWebView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebviewFragment : BaseFragment<FragmentWebviewBinding>(R.layout.fragment_webview) {

    private val args: WebviewFragmentArgs by navArgs()

    override fun initViews() = with(binding) {
        super.initViews()
        thisFragment = this@WebviewFragment

        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.cacheMode = LOAD_CACHE_ELSE_NETWORK

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                swipeRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipeRefresh.isRefreshing = false
            }
        }

        webView.setOnScrollChangedCallback(object : ObservableWebView.OnScrollChangedCallback {
            override fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int) {
                swipeRefresh.isEnabled = false
                if(t > oldt) viewLine.isVisible = true
                if(t < oldt) viewLine.isVisible = true
                if(t == 0) {
                    swipeRefresh.isEnabled = true
                    viewLine.isVisible = false
                }
            }
        })

        webView.loadUrl(args.url)

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            webView.loadUrl(args.url)
        }
    }

    fun onBack() = findNavController().popBackStack()

    override fun onPause() {
        super.onPause()
        binding.webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }
}