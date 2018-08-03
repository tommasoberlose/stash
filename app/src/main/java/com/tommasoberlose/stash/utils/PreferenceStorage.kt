package com.tommasoberlose.stash.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.SystemClock
import android.preference.PreferenceManager
import com.tommasoberlose.stash.constants.PreferenceConstants

object PreferenceStorage {

  @SuppressLint("ApplySharedPref")
  fun enableStash(context: Context) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    sharedPreferences.edit()
        .putBoolean(PreferenceConstants.PREFERENCE_STASHING, true)
        .putLong(PreferenceConstants.PREFERENCE_STASHING_START_TIME, SystemClock.currentThreadTimeMillis())
        .commit()
  }

  @SuppressLint("ApplySharedPref")
  fun disableStash(context: Context) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    sharedPreferences.edit()
        .putBoolean(PreferenceConstants.PREFERENCE_STASHING, false)
        .putLong(PreferenceConstants.PREFERENCE_STASHING_START_TIME, 0)
        .commit()
  }

  fun isStashing(context: Context): Boolean {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    return sharedPreferences.getBoolean(PreferenceConstants.PREFERENCE_STASHING, false)
  }

  fun getStashingStartTime(context: Context): Long {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    return sharedPreferences.getLong(PreferenceConstants.PREFERENCE_STASHING_START_TIME, 0)
  }

}