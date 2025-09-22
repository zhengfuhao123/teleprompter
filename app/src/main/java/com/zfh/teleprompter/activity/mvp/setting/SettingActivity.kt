package com.zfh.teleprompter.activity.mvp.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.tencent.upgrade.bean.UpgradeStrategy
import com.tencent.upgrade.core.UpgradeManager
import com.tencent.upgrade.core.UpgradeReqCallbackForUserManualCheck
import com.zfh.teleprompter.activity.BaseActivity
import com.zfh.teleprompter.activity.mvp.setting.history.HistoryActivity
import com.zfh.teleprompter.activity.mvp.setting.theme.ThemeActivity
import com.zfh.teleprompter.databinding.ActivitySettingBinding
import com.zfh.teleprompter.ext.gone
import com.zfh.teleprompter.ext.visible
import com.zfh.teleprompter.utils.AppUtils


class SettingActivity : BaseActivity() {

    companion object {
        private const val TAG = "SettingActivity"
        fun start(context: Context) {
            val intent = Intent(context, SettingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
        }
    }

    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayoutView(binding.root)

        binding.settingTheme.setOnClickListener {
            ThemeActivity.start(this)
        }

        binding.settingHistory.setOnClickListener {
            HistoryActivity.start(this)
        }

        binding.settingVersion.setOnClickListener {
            setCheckingUpgrade(true)
            //用户主动触发检查更新
            UpgradeManager.getInstance()
                .checkUpgrade(true, null, object : UpgradeReqCallbackForUserManualCheck() {
                    override fun onReceiveStrategy(p0: UpgradeStrategy?) {
                        super.onReceiveStrategy(p0)
                        setCheckingUpgrade(false)
                    }

                    override fun onFail(p0: Int, p1: String?) {
                        super.onFail(p0, p1)
                        setCheckingUpgrade(false)
                    }

                    override fun onReceivedNoStrategy() {
                        super.onReceivedNoStrategy()
                        Toast.makeText(this@SettingActivity, "已经是最新版本了", Toast.LENGTH_SHORT)
                            .show()
                        setCheckingUpgrade(false)
                    }
                })
        }

        binding.settingAbout.gone()

        binding.settingVersionText.text = AppUtils.getVersionName(this)
        setCheckingUpgrade(false)
    }

    private fun setCheckingUpgrade(checking: Boolean) {
        if (checking) {
            binding.settingVersionProgress.visible()
            binding.settingVersionText.gone()
        } else {
            binding.settingVersionProgress.gone()
            binding.settingVersionText.visible()
        }
    }

    private fun refreshThemeMode() {
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                binding.settingThemeModeText.text = "深色模式"
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                binding.settingThemeModeText.text = "浅色模式"
            }

            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                binding.settingThemeModeText.text = "跟随系统"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshThemeMode()
    }
}