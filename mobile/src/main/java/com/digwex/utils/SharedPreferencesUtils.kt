package com.digwex.utils

import android.content.SharedPreferences

object SharedPreferencesUtils {

  fun putDouble(edit: SharedPreferences.Editor,
                key: String, value: Double): SharedPreferences.Editor {
    return edit.putLong(key, java.lang.Double.doubleToRawLongBits(value))
  }

  fun getDouble(prefs: SharedPreferences, key: String,
                defaultValue: Double): Double {
    return java.lang.Double.longBitsToDouble(prefs.getLong(key, java.lang.Double.doubleToLongBits(defaultValue)))
  }

  fun putString(prefs: SharedPreferences,
                key: String,
                value: String) {
    prefs.edit().putString(key, value).apply()
  }

  fun getString(prefs: SharedPreferences,
                key: String,
                defaultValue: String): String {
    return prefs.getString(key, defaultValue)?:defaultValue
  }

  fun putInt(prefs: SharedPreferences,
             key: String,
             value: Int) {
    prefs.edit().putInt(key, value).apply()
  }

  fun getInt(prefs: SharedPreferences,
             key: String,
             defaultValue: Int): Int {
    return prefs.getInt(key, defaultValue)
  }

  fun putLong(prefs: SharedPreferences,
              key: String,
              value: Long) {
    prefs.edit().putLong(key, value).apply()
  }

  fun getLong(prefs: SharedPreferences,
              key: String,
              defaultValue: Long): Long {
    return prefs.getLong(key, defaultValue)
  }

  fun putBoolean(prefs: SharedPreferences,
                 key: String,
                 value: Boolean) {
    prefs.edit().putBoolean(key, value).apply()
  }

  fun getBoolean(prefs: SharedPreferences,
                 key: String,
                 defaultValue: Boolean): Boolean {
    return prefs.getBoolean(key, defaultValue)
  }
}
