package com.zfh.teleprompter.activity.mvp.setting.theme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.zfh.teleprompter.activity.BaseActivity
import com.zfh.teleprompter.databinding.ActivityThemeBinding
import com.zfh.teleprompter.ext.gone
import com.zfh.teleprompter.ext.visible
import com.zfh.teleprompter.utils.ThemeHelper

class ThemeActivity : BaseActivity() {

    companion object {
        private const val TAG = "ThemeActivity"

        fun start(context: Context) {
            val intent = Intent(context, ThemeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
        }
    }

    private val binding by lazy { ActivityThemeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleText("主题设置")
        setContentLayoutView(binding.root)

        refreshUI()

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            ThemeHelper.setFollowSys(isChecked)
            refreshUI()
        }

        binding.themeLight.setOnClickListener {
            ThemeHelper.setLight()
        }
        binding.themeNight.setOnClickListener {
            ThemeHelper.setNight()
        }
    }

    private fun refreshUI() {
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                binding.themeSwitch.isChecked = true
                binding.themeManualSettings.gone()
            }

            AppCompatDelegate.MODE_NIGHT_YES -> {
                //深色
                binding.themeSwitch.isChecked = false
                binding.themeManualSettings.visible()
                binding.themeCheckNight.visible()
                binding.themeCheckLight.gone()
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                //浅色
                binding.themeSwitch.isChecked = false
                binding.themeManualSettings.visible()
                binding.themeCheckNight.gone()
                binding.themeCheckLight.visible()
            }
        }
    }
}