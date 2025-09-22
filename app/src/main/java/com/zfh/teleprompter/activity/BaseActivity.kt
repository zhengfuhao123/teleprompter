package com.zfh.teleprompter.activity

import android.app.Dialog
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.zfh.teleprompter.R

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var mBackBtn: ImageView
    private lateinit var mTitle: TextView
    private lateinit var mContentContainer: LinearLayout

    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        super.setContentView(getFrameLayoutId())
        if (isSystemBarTransparent()) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.activity_bg)
            val night =
                (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            if (Build.VERSION.SDK_INT >= 31) {
                val controller = WindowCompat.getInsetsController(window, window.decorView)
                controller.isAppearanceLightStatusBars = !night
                controller.isAppearanceLightNavigationBars = !night
            }
        }
        mContentContainer = findViewById(R.id.care_base_content)
        mBackBtn = findViewById(R.id.base_back)
        mTitle = findViewById(R.id.base_title)
        mBackBtn.setOnClickListener { onBackViewClick() }
    }

    @Synchronized
    protected open fun onBackViewClick() {
        onBackPressed()
    }

    protected open fun getFrameLayoutId(): Int {
        return R.layout.activity_baes
    }

    protected open fun isSystemBarTransparent(): Boolean {
        return true
    }

    fun setTitleText(title: String) {
        mTitle.text = title
    }

    fun setTitleText(resId: Int) {
        mTitle.setText(resId)
    }

    fun getTitleView(): TextView {
        return mTitle
    }

    protected open fun setContentLayoutView(pLayoutID: Int) {
        val view = LayoutInflater.from(this).inflate(pLayoutID, null)
        view.layoutParams = LinearLayout.LayoutParams(-1, -1)
        mContentContainer.addView(view)
    }

    protected open fun setContentLayoutView(view: View) {
        mContentContainer.addView(view)
    }

    /**
     * 显示自定义弹窗
     */
    fun showProgressDialog(text: String) {
        if (progressDialog == null) {
            // 创建弹窗
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_base, null)
            progressDialog = Dialog(this).apply {
                setContentView(dialogView)
                setCancelable(false) // 点击外部是否关闭弹窗
                window?.setBackgroundDrawableResource(android.R.color.transparent) // 去除默认背景
            }

        }

        progressDialog?.findViewById<TextView>(R.id.base_message)?.text = text

        // 显示弹窗
        progressDialog?.show()
    }

    /**
     * 隐藏自定义弹窗
     */
    fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 释放弹窗资源
        progressDialog?.dismiss()
        progressDialog = null
    }
}