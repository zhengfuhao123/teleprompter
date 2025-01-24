package com.zfh.teleprompter.app

import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        const val TAG = "App"
        private var context: Context? = null
        private var app: App? = null
        fun getContext(): Context? {
            return context
        }

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
    }

}