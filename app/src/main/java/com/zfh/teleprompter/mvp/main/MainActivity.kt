package com.zfh.teleprompter.mvp.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.zfh.teleprompter.service.FloatingViewService
import com.zfh.teleprompter.R

class MainActivity : AppCompatActivity(),Contract.View {

    companion object{
        private const val OVERLAY_PERMISSION_REQ_CODE = 1234
    }

    private lateinit var presenter : Contract.Presenter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)

        val editTextFloatingContent = findViewById<EditText>(R.id.editTextFloatingContent)
        val buttonUpdateText = findViewById<Button>(R.id.buttonUpdateText)
        val buttonIncreaseTextSize = findViewById<Button>(R.id.increaseTextSize)
        val buttonReduceTextSize = findViewById<Button>(R.id.reduceTextSize)
        val switchFloatingView = findViewById<Switch>(R.id.switchFloatingView)

        buttonUpdateText.setOnClickListener {
            val newText = editTextFloatingContent.text.toString()
            presenter.setText(newText)
        }

        buttonIncreaseTextSize.setOnClickListener {
            presenter.increaseTextSize()
        }

        buttonReduceTextSize.setOnClickListener {
            presenter.reduceTextSize()
        }

        switchFloatingView.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                    requestOverlayPermission()
                } else {
                    startFloatingViewService()
                }
            } else {
                stopFloatingViewService()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            switchFloatingView.isChecked = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                startFloatingViewService()
            } else {
                findViewById<Switch>(R.id.switchFloatingView).isChecked = false
            }
        }
    }

    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
    }

    private fun startFloatingViewService() {
        val intent = Intent(this, FloatingViewService::class.java)
        startService(intent)
    }

    private fun stopFloatingViewService() {
        stopService(Intent(this, FloatingViewService::class.java))
    }

    private fun updateFloatingViewText(newText: String) {
        val intent = Intent("UPDATE_FLOATING_TEXT")
        intent.putExtra("text", newText)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}