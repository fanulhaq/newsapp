/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

@file:Suppress("DEPRECATION")

package com.muchi.newsapp.ui.common

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.muchi.newsapp.R
import com.muchi.newsapp.databinding.ViewSnackbarBinding

private fun Context.snackBar(
    layoutInflater: LayoutInflater, v: View,
    message: String?, image: Int, background: Int, color: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    position: Int = 0, paddingTop: Int = 0, paddingBottom: Int = 0
) = try {
    val snackbar = Snackbar.make(v, "", duration)
    snackbar.view.setBackgroundColor(Color.TRANSPARENT)
    val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
    snackbarLayout.setPadding(0, paddingTop, 0, paddingBottom)

    val binding = ViewSnackbarBinding.inflate(layoutInflater)
    with(binding) {
        tvMsg.text = message ?: "Unknown Message"
        imageView.setImageResource(image)

        tvMsg.setTextColor(resources.getColor(color))
        imageView.setColorFilter(resources.getColor(color))
        cvText.setCardBackgroundColor(resources.getColor(background))
        cvText.strokeColor = resources.getColor(color)

        snackbarLayout.addView(root, 0)
    }

    when(position) {
        1 -> {
            val params = snackbar.view.layoutParams
            if (params is CoordinatorLayout.LayoutParams) {
                params.gravity = Gravity.TOP
            } else {
                (params as FrameLayout.LayoutParams).gravity = Gravity.TOP
            }
            snackbar.view.layoutParams = params
        }
        2 -> {
            val params = snackbar.view.layoutParams
            if (params is CoordinatorLayout.LayoutParams) {
                params.gravity = Gravity.CENTER
            } else {
                (params as FrameLayout.LayoutParams).gravity = Gravity.CENTER
            }
            snackbar.view.layoutParams = params
        }
    }
    snackbar.show()
} catch (e: IllegalArgumentException) {
    e.printStackTrace()
}

fun Context.snackBarWarning(
    layoutInflater: LayoutInflater, v: View, message: String?,
    duration: Int = Snackbar.LENGTH_SHORT,
    position: Int = 0, padding: Int = 0)
= snackBar(
    layoutInflater= layoutInflater,
    v = v,
    message = message,
    image = R.drawable.ic_warning,
    background = R.color.backgroundSnackBarWarning,
    color = R.color.colorSnackBarWarning,
    duration = duration,
    position = position,
    paddingTop = if(position == 1) padding else 0,
    paddingBottom = if(position == 0) padding else 0
)

fun Context.snackBarSuccess(
    layoutInflater: LayoutInflater, v: View, message: String?,
    duration: Int = Snackbar.LENGTH_SHORT,
    position: Int = 0, padding: Int = 0)
= snackBar(
    layoutInflater= layoutInflater,
    v = v,
    message = message,
    image = R.drawable.ic_success,
    background = R.color.backgroundSnackBarSuccess,
    color = R.color.colorSnackBarSuccess,
    duration = duration,
    position = position,
    paddingTop = if(position == 1) padding else 0,
    paddingBottom = if(position == 0) padding else 0
)

fun Context.snackBarError(
    layoutInflater: LayoutInflater, v: View, message: String?,
    duration: Int = Snackbar.LENGTH_SHORT,
    position: Int = 0, padding: Int = 0)
= snackBar(
    layoutInflater= layoutInflater,
    v = v,
    message = message,
    image = R.drawable.ic_error,
    background = R.color.backgroundSnackBarError,
    color = R.color.colorSnackBarError,
    duration = duration,
    position = position,
    paddingTop = if(position == 1) padding else 0,
    paddingBottom = if(position == 0) padding else 0
)