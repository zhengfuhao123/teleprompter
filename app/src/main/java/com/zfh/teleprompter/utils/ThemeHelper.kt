package com.zfh.teleprompter.utils

import androidx.appcompat.app.AppCompatDelegate

object ThemeHelper {

    const val KEY_MODE = "ThemeLightMode"
    const val KEY_IS_FOLLOW = "ThemeIsFollowSys"

    fun loadTheme() {
        if (SPHelper.getBoolean(KEY_IS_FOLLOW, true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            AppCompatDelegate.setDefaultNightMode(
                SPHelper.getInt(
                    KEY_MODE,
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            )
        }
    }

    fun setFollowSys(isFollow: Boolean) {
        if (isFollow) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            SPHelper.putBoolean(KEY_IS_FOLLOW, true)
        } else {
            AppCompatDelegate.setDefaultNightMode(
                SPHelper.getInt(
                    KEY_MODE,
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            )
            SPHelper.putBoolean(KEY_IS_FOLLOW, false)
        }
    }

    fun setLight() {
        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            SPHelper.putInt(KEY_MODE, AppCompatDelegate.MODE_NIGHT_NO)
            SPHelper.putBoolean(KEY_IS_FOLLOW, false)
        }
    }

    fun setNight() {
        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            SPHelper.putInt(KEY_MODE, AppCompatDelegate.MODE_NIGHT_YES)
            SPHelper.putBoolean(KEY_IS_FOLLOW, false)
        }
    }

}