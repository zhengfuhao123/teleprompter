package com.zfh.teleprompter.mvp.main

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.zfh.teleprompter.app.App
import com.zfh.teleprompter.service.FloatingViewService
import com.zfh.teleprompter.Options

class MainPresenter(val view: Contract.View) : Contract.Presenter {

    override fun setText(msg: String) {
        Options.mText = msg
        val intent = Intent(FloatingViewService.UPDATE_FLOATING_TEXT)
        App.getContext()?.let { LocalBroadcastManager.getInstance(it).sendBroadcast(intent) }
    }

    override fun setTextSize(size: Int) {
        val intent = Intent(FloatingViewService.UPDATE_FLOATING_TEXT_SIZE)
        App.getContext()?.let { LocalBroadcastManager.getInstance(it).sendBroadcast(intent) }
    }

    override fun increaseTextSize() {
        val temp = Options.mTextSize + 1
        if (temp in 16..30) {
            Options.mTextSize++
        }
        val intent = Intent(FloatingViewService.UPDATE_FLOATING_TEXT_SIZE)
        App.getContext()?.let { LocalBroadcastManager.getInstance(it).sendBroadcast(intent) }
    }

    override fun reduceTextSize() {
        val temp = Options.mTextSize - 1
        if (temp in 16..30) {
            Options.mTextSize--
        }
        val intent = Intent(FloatingViewService.UPDATE_FLOATING_TEXT_SIZE)
        App.getContext()?.let { LocalBroadcastManager.getInstance(it).sendBroadcast(intent) }
    }

    override fun setTextColor(color: Int) {

    }

}