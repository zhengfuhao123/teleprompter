package com.zfh.teleprompter.mvp.main

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.zfh.teleprompter.Options
import com.zfh.teleprompter.R
import com.zfh.teleprompter.app.App
import com.zfh.teleprompter.ext.gone
import com.zfh.teleprompter.ext.selectColor
import com.zfh.teleprompter.ext.visible

class HomeActivity : AppCompatActivity(), Contract.View {

    companion object {
        private const val FLOAT_TAG = "teleprompter"
    }

    private lateinit var mEt: EditText
    private lateinit var mEtClearView: View
    private lateinit var mImportView: View
    private lateinit var mSaveView: View
    private lateinit var mShowView: TextView
    private lateinit var mColorPickView: View
    private lateinit var mTextColorView: View
    private lateinit var mBackgroundColorView: View
    private lateinit var mTextSizeSeekBar: SeekBar
    private lateinit var mTextSizeTextView: TextView

    private lateinit var presenter: Contract.Presenter
    private var isShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        presenter = MainPresenter(this)

        mEt = findViewById(R.id.home_et)
        mEtClearView = findViewById(R.id.home_et_clear)
        mImportView = findViewById(R.id.home_import)
        mSaveView = findViewById(R.id.home_save)
        mShowView = findViewById(R.id.home_exhibit)
        mColorPickView = findViewById(R.id.home_color_pick)
        mTextColorView =
            findViewById<View>(R.id.home_text_color).apply { setBackgroundColor(Options.mTextColor) }
        mBackgroundColorView =
            findViewById<View>(R.id.home_background_color).apply { setBackgroundColor(Options.mBackgroundColor) }

        mTextSizeSeekBar = findViewById(R.id.home_text_size_seekbar)

        mTextSizeTextView = findViewById(R.id.home_text_size_tv)

        mTextSizeSeekBar.progress = Options.mTextSize
        mTextSizeTextView.text = Options.mTextSize.toString()

        mTextSizeSeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                presenter.setTextSize(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        mImportView.setOnClickListener {
            importClipboard(mEt)
        }

        mEtClearView.setOnClickListener {
            mEt.setText("")
        }

        mSaveView.setOnClickListener {
            presenter.setText(mEt.text.toString())
            Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show()
        }

        mColorPickView.setOnClickListener {
            selectColor { color ->
                presenter.setTextColor(color)
            }
        }

        mBackgroundColorView.setOnClickListener {
            selectColor { color ->
                presenter.setBackgroundColor(color)
            }
        }

        mShowView.setOnClickListener {
            isShowing = if (isShowing) {
                it.setBackgroundResource(R.drawable.bg_rounded_8_212936)
                EasyFloat.dismiss(FLOAT_TAG)
                false
            } else {
                it.setBackgroundResource(R.drawable.bg_rounded_8_00aa00)
                EasyFloat.with(this)
                    .setLayout(R.layout.layout_floating_view) { float ->
                        initFloat(float)
                    }
                    .setShowPattern(ShowPattern.ALL_TIME)
                    .setDragEnable(true)
                    .setGravity(Gravity.CENTER)
                    .setTag(FLOAT_TAG)
                    //.setFilter(HomeActivity::class.java)
                    .show()
                true
            }
        }

        mEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    mEtClearView.gone()
                } else {
                    mEtClearView.visible()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        mEt.setText(Options.mText)
    }

    private fun initFloat(view: View) {
        view.setOnClickListener {
            val intent = Intent(App.getApp(), HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
        view.setBackgroundColor(Options.mBackgroundColor)

        val textView = view.findViewById<TextView>(R.id.floatingText)

        textView.text = Options.mText
        textView.textSize = Options.mTextSize.toFloat()
        textView.setTextColor(Options.mTextColor)
    }

    private fun importClipboard(et: EditText) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboardManager.hasPrimaryClip() && clipboardManager.primaryClip!!.itemCount > 0) {
            val clipText = clipboardManager.primaryClip!!.getItemAt(0).text
            if (clipText != null) {
                et.setText(clipText)
                Toast.makeText(this, R.string.import_success, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.import_fail, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, R.string.import_fail, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTextSizeChange() {
        val view = EasyFloat.getFloatView(FLOAT_TAG)
        view?.findViewById<TextView>(R.id.floatingText)?.textSize = Options.mTextSize.toFloat()
        mTextSizeTextView.text = Options.mTextSize.toString()
    }

    override fun onTextChange() {
        val view = EasyFloat.getFloatView(FLOAT_TAG)
        view?.findViewById<TextView>(R.id.floatingText)?.text = Options.mText
    }

    override fun onTextColorChange() {
        val view = EasyFloat.getFloatView(FLOAT_TAG)
        view?.findViewById<TextView>(R.id.floatingText)?.setTextColor(Options.mTextColor)
        mTextColorView.setBackgroundColor(Options.mTextColor)
    }

    override fun onBackgroundChange() {
        val view = EasyFloat.getFloatView(FLOAT_TAG)
        view?.setBackgroundColor(Options.mBackgroundColor)
        mBackgroundColorView.setBackgroundColor(Options.mBackgroundColor)
    }
}