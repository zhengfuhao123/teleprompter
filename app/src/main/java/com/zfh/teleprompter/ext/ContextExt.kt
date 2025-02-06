package com.zfh.teleprompter.ext

import android.content.Context
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder


fun Context.selectColor(onSelected: (Int) -> Unit) {
    ColorPickerDialogBuilder
        .with(this)
        .setTitle("Choose color")
        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
        .density(12)
        .setPositiveButton(
            "ok",
        ) { _, selectedColor, allColors -> onSelected(selectedColor) }
        .setNegativeButton(
            "cancel"
        ) { _, _ -> }
        .build()
        .show()
}