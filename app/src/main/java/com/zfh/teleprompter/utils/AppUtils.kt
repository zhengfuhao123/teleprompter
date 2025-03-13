package com.zfh.teleprompter.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings

object AppUtils {

    /**
     * 获取应用的版本名称（versionName）
     * @param context 上下文对象
     * @return 版本名称，如果获取失败则返回 "Unknown"
     */
    fun getVersionName(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "Unknown"
        }
    }

    /**
     * 获取应用的版本号（versionCode）
     * @param context 上下文对象
     * @return 版本号，如果获取失败则返回 -1
     */
    fun getVersionCode(context: Context): Int {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            -1
        }
    }

    /**
     * 判断应用是否安装
     * @param context 上下文对象
     * @param packageName 目标应用的包名
     * @return 是否安装
     */
    fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0) != null
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * 打开应用的设置页面
     * @param context 上下文对象
     */
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(intent)
    }

    /**
     * 打开浏览器访问指定 URL
     * @param context 上下文对象
     * @param url 目标 URL
     */
    fun openBrowser(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    /**
     * 打开应用商店
     * @param context 上下文对象
     * @param packageName 目标应用的包名
     */
    fun openAppStore(context: Context, packageName: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // 如果没有安装应用商店，跳转到网页版
            openBrowser(context, "https://play.google.com/store/apps/details?id=$packageName")
        }
    }

    /**
     * 获取设备的系统信息
     * @return 系统信息字符串
     */
    fun getSystemInfo(): String {
        return "Manufacturer: ${Build.MANUFACTURER}, Model: ${Build.MODEL}, SDK: ${Build.VERSION.SDK_INT}"
    }

    /**
     * 判断设备是否运行在 Android 6.0 (API 23) 及以上版本
     * @return 是否运行在 Android 6.0 及以上
     */
    fun isMarshmallowOrAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     * 判断设备是否运行在 Android 10 (API 29) 及以上版本
     * @return 是否运行在 Android 10 及以上
     */
    fun isAndroid10OrAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
}
