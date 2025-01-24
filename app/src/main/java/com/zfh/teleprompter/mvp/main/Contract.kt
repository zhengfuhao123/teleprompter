package com.zfh.teleprompter.mvp.main

interface Contract {

    interface View {

    }

    interface Presenter {
        fun setText(msg: String)

        fun setTextSize(size: Int)

        fun increaseTextSize()

        fun reduceTextSize()

        fun setTextColor(color: Int)
    }

}