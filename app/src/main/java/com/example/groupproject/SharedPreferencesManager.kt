package com.example.groupproject

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val PREF_NAME = "AppPreferences"
    private lateinit var preferences: SharedPreferences

    // Initialize SharedPreferences
    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun saveFloat(key: String, value: Float) {
        preferences.edit().putFloat(key, value).apply()
    }

    // Retrieve data
    fun getString(key: String, defaultValue: String = ""): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float = 0.0f): Float {
        return preferences.getFloat(key, defaultValue)
    }

    // Clear all preferences
    fun clearAll() {
        preferences.edit().clear().apply()
    }
}