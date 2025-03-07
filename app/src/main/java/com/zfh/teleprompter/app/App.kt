package com.zfh.teleprompter.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.tencent.upgrade.bean.UpgradeConfig
import com.tencent.upgrade.core.DefaultUpgradeStrategyRequestCallback
import com.tencent.upgrade.core.UpgradeManager
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
        initShiply()
    }

    // 初始化OTA组件
    private fun initShiply() {
        // 构造初始化参数
        val config = UpgradeConfig.Builder()
            .appId("2eaad712bf")
            .appKey("e69e7fdc-50f4-46c4-a8af-e6bbf82f16ab")
            .build()

        // 初始化SDK
        UpgradeManager.getInstance().init(this, config)

        //App 首次启动时自动检查更新
        UpgradeManager.getInstance()
            .checkUpgrade(false, null, DefaultUpgradeStrategyRequestCallback())

        //用户主动触发检查更新
//        UpgradeManager.getInstance()
//            .checkUpgrade(true, null, UpgradeReqCallbackForUserManualCheck())

    }

}