/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections


fun NavController.safetyNavigate(@IdRes resId: Int, args: Bundle? = null) =
    try { navigate(resId, args) }
    catch (e: Exception) { e.printStackTrace() }

fun NavController.safetyNavigate(direction: NavDirections) =
    try { navigate(direction) }
    catch (e: Exception) { e.printStackTrace() }