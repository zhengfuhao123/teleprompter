package com.zfh.teleprompter

import android.graphics.Color
import com.zfh.teleprompter.utils.SPHelper

object Options {

    const val KEY_OPTIONS_TEXT = "optionsText"
    const val KEY_OPTIONS_TEXT_SIZE = "optionsTextSize"
    const val KEY_OPTIONS_TEXT_COLOR = "optionsTextColor"
    const val KEY_OPTIONS_BACKGROUND_COLOR = "optionsBackgroundColor"
    const val KEY_OPTIONS_IMAGE_ALPHA = "optionsImageAlpha"

    var initialized = false

    var mText: String = "默认文本"
        set(value) {
            if (initialized) {
                SPHelper.putString(KEY_OPTIONS_TEXT, value)
            }
            field = value
        }

    var mTextSize: Int = 16
        set(value) {
            if (initialized) {
                SPHelper.putInt(KEY_OPTIONS_TEXT_SIZE, value)
            }
            field = value
        }

    var mTextColor: Int = Color.BLACK
        set(value) {
            if (initialized) {
                SPHelper.putInt(KEY_OPTIONS_TEXT_COLOR, value)
            }
            field = value
        }

    var mBackgroundColor: Int = Color.TRANSPARENT
        set(value) {
            if (initialized) {
                SPHelper.putInt(KEY_OPTIONS_BACKGROUND_COLOR, value)
            }
            field = value
        }

    var mImageAlpha: Int = 255
        set(value) {
            if (initialized) {
                SPHelper.putInt(KEY_OPTIONS_IMAGE_ALPHA, value)
            }
            field = value
        }

    fun init() {
        mText = SPHelper.getString(KEY_OPTIONS_TEXT, "默认文本") ?: "默认文本"
        mTextSize = SPHelper.getInt(KEY_OPTIONS_TEXT_SIZE, 16)
        mTextColor = SPHelper.getInt(KEY_OPTIONS_TEXT_COLOR, Color.BLACK)
        mBackgroundColor = SPHelper.getInt(KEY_OPTIONS_BACKGROUND_COLOR, Color.TRANSPARENT)
        mImageAlpha = SPHelper.getInt(KEY_OPTIONS_IMAGE_ALPHA, 255)
        initialized = true
    }
}