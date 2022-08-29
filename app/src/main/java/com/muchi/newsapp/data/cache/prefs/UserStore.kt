/*
 * Copyright (c) 2022 ~ Irfanul Haq.
 */

package com.muchi.newsapp.data.cache.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class UserStore @Inject constructor(
    @ActivityContext private val context: Context
) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "datauser")
        private val SEARCH = intPreferencesKey("search")
        private val TAB_POSITION = intPreferencesKey("tabPosition")
    }

    suspend fun clear() = context.dataStore.edit {
        it.clear()
    }

    val search: Flow<Int>
        get() = context.dataStore.data.map { preferences ->
            preferences[SEARCH] ?: 0
        }

    suspend fun search(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[SEARCH] = value
        }
    }

    val tabPosition: Flow<Int>
        get() = context.dataStore.data.map { preferences ->
            preferences[TAB_POSITION] ?: -1
        }

    suspend fun tabPosition(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[TAB_POSITION] = value
        }
    }
}