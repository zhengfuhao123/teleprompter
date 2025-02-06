package com.zfh.teleprompter.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.zfh.teleprompter.Options
import com.zfh.teleprompter.utils.SPHelper

class App : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        @SuppressLint("StaticFieldLeak")
        private var app: App? = null

        fun getApp(): App? {
            return app
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        context = this
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        SPHelper.init(this)
        Options.init()
    }

}