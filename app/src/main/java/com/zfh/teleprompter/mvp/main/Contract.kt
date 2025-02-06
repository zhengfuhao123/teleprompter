package com.zfh.teleprompter.mvp.main

interface Contract {

    interface View {
        fun onTextSizeChange()
        fun onTextChange()
        fun onTextColorChange()
        fun onBackgroundChange()
    }

    interface Presenter {
        fun setText(msg: String)

        fun setTextSize(size: Int)

        fun increaseTextSize()

        fun reduceTextSize()

        fun setTextColor(color: Int)

        fun setBackgroundColor(color: Int)
    }

}