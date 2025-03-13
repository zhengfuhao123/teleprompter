package com.zfh.teleprompter.activity.mvp.main

import com.zfh.teleprompter.Options

class MainPresenter(private val view: HomeContract.View) : HomeContract.Presenter {

    override fun setText(msg: String) {
        if (msg.isEmpty()) {
            Options.mText = "默认文本"
        } else {
            Options.mText = msg
        }
        view.onTextChange()
    }

    override fun setTextSize(size: Int) {
        Options.mTextSize = size
        view.onTextSizeChange()
    }

    override fun increaseTextSize() {
        val temp = Options.mTextSize + 1
        if (temp in 10..30) {
            Options.mTextSize++
        }
        view.onTextSizeChange()
    }

    override fun reduceTextSize() {
        val temp = Options.mTextSize - 1
        if (temp in 10..30) {
            Options.mTextSize--
        }
        view.onTextSizeChange()
    }

    override fun setTextColor(color: Int) {
        Options.mTextColor = color
        view.onTextColorChange()
    }

    override fun setBackgroundColor(color: Int) {
        Options.mBackgroundColor = color
        view.onBackgroundChange()
    }

}