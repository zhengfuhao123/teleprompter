package com.zfh.teleprompter.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.zfh.teleprompter.Options
import com.zfh.teleprompter.R
import org.greenrobot.eventbus.EventBus

class FloatingViewService : Service() {

    companion object{
        const val UPDATE_FLOATING_TEXT = "UPDATE_FLOATING_TEXT"
        const val UPDATE_FLOATING_TEXT_SIZE = "UPDATE_FLOATING_TEXT_SIZE"
    }

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private lateinit var floatingText: TextView
    private lateinit var params: WindowManager.LayoutParams

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                UPDATE_FLOATING_TEXT -> updateFloatingText()
                UPDATE_FLOATING_TEXT_SIZE -> updateFloatingTextSize()
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        EventBus.getDefault().register(this)

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_view, null)
        floatingText = floatingView.findViewById(R.id.floatingText)

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = resources.displayMetrics.widthPixels / 3
            y = resources.displayMetrics.heightPixels / 3
        }

        windowManager.addView(floatingView, params)

        setupTouchListener()

        val filter = IntentFilter()
        filter.addAction(UPDATE_FLOATING_TEXT)
        filter.addAction(UPDATE_FLOATING_TEXT_SIZE)

        LocalBroadcastManager.getInstance(this).registerReceiver(
            broadcastReceiver,
            filter
        )

        updateFloatingText()
        updateFloatingTextSize()
    }

    private fun updateFloatingText() {
        floatingText.text = Options.mText
    }

    private fun updateFloatingTextSize() {
        floatingText.textSize = Options.mTextSize.toFloat()
    }

    private fun updateFloatingTextColor(color: Int) {
        floatingText.setTextColor(color)
    }

    private fun setupTouchListener() {
        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f

        floatingView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = initialX + (event.rawX - initialTouchX).toInt()
                    val newY = initialY + (event.rawY - initialTouchY).toInt()

                    // 限制悬浮窗不超出屏幕边界
                    params.x =
                        newX.coerceIn(0, windowManager.defaultDisplay.width - floatingView.width)
                    params.y =
                        newY.coerceIn(0, windowManager.defaultDisplay.height - floatingView.height)

                    windowManager.updateViewLayout(floatingView, params)
                    true
                }
                else -> false
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(floatingView)
        EventBus.getDefault().unregister(this)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }
}