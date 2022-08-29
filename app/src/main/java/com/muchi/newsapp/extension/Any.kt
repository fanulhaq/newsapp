/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.muchi.newsapp.R
import java.text.ParseException
import java.text.SimpleDateFormat


fun Context?.isValidContextForGlide(): Boolean {
    if (this == null) {
        return false
    }
    if (this is Activity) {
        if (this.isDestroyed || this.isFinishing) {
            return false
        }
    }
    return true
}

fun Context.loadImageUrl(
    imageView: ImageView,
    url: Any,
    @DrawableRes placeholder: Int = R.drawable.ic_photos_placeholder
) {
    if(this is Activity) {
        if(this.isDestroyed || this.isFinishing) return
    }

    Glide.with(this)
        .load(url)
        .placeholder(placeholder)
        .into(imageView)
}

@SuppressLint("SimpleDateFormat")
fun String.formatterDate(
    oldFormat: String = "yyyy-MM-dd'T'HH:mm:ss'Z'",
    newFormat: String = "dd MMM yyyy • hh:mm"
) : String {
    val formatter = SimpleDateFormat(oldFormat)
    val returnFormatter = SimpleDateFormat(newFormat)

    return try {
        val date = formatter.parse(this)
        if (date != null) {
            returnFormatter.format(date)
        } else this
    } catch (e: ParseException) {
        e.printStackTrace()
        this
    } catch (e: Exception) {
        e.printStackTrace()
        this
    }
}