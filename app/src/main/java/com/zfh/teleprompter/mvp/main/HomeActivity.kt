package com.zfh.teleprompter.mvp.main

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.zfh.teleprompter.Options
import com.zfh.teleprompter.R
import com.zfh.teleprompter.app.App
import com.zfh.teleprompter.databinding.ActivityHomeBinding
import com.zfh.teleprompter.ext.gone
import com.zfh.teleprompter.ext.selectColor
import com.zfh.teleprompter.ext.visible
import com.zfh.teleprompter.widget.ScaleImage
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions


@RuntimePermissions
class HomeActivity : AppCompatActivity(), Contract.View {

    companion object {
        private const val FLOAT_TEXT_TAG = "textFloat"
        private const val FLOAT_IMAGE_TAG = "imageFloat"
    }

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private lateinit var presenter: Contract.Presenter
    private var isShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        presenter = MainPresenter(this)
        binding.homeTextColor.setBackgroundColor(Options.mTextColor)
        binding.homeBackgroundColor.setBackgroundColor(Options.mBackgroundColor)
        binding.homeTextSizeSeekbar.progress = Options.mTextSize
        binding.homeTextSizeTv.text = Options.mTextSize.toString()

        binding.homeTextSizeSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                presenter.setTextSize(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        binding.homeImport.setOnClickListener {
            importClipboard(binding.homeEt)
        }

        binding.homeEtClear.setOnClickListener {
            binding.homeEt.setText("")
        }

        binding.homeSave.setOnClickListener {
            presenter.setText(binding.homeEt.text.toString())
            Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show()
        }

        binding.homeColorPick.setOnClickListener {
            selectColor { color ->
                presenter.setTextColor(color)
            }
        }

        binding.homeBackgroundColor.setOnClickListener {
            selectColor { color ->
                presenter.setBackgroundColor(color)
            }
        }

        binding.homeExhibit.setOnClickListener {
            isShowing = if (isShowing) {
                it.setBackgroundResource(R.drawable.bg_rounded_8_212936)
                EasyFloat.dismiss(FLOAT_TEXT_TAG)
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
                    .setTag(FLOAT_TEXT_TAG)
                    //.setFilter(HomeActivity::class.java)
                    .show()
                true
            }
        }

        binding.homeEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.homeEtClear.gone()
                } else {
                    binding.homeEtClear.visible()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.homeEt.setText(Options.mText)

        binding.homeShowImage.setOnClickListener {
            openImagePicker()
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    private fun openImagePicker() {
        getContent.launch("image/*")
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    private fun onReadExternalDenied() {
        Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show()
    }

    // 在 Activity 中定义 ActivityResultLauncher
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                showAppFloat2(it)
            }
        }

    private fun showAppFloat2(imageUri: Uri) {
        EasyFloat.with(this.applicationContext)
            .setTag(FLOAT_IMAGE_TAG)
            .setShowPattern(ShowPattern.ALL_TIME)
            .setGravity(Gravity.CENTER)
            .setLayout(R.layout.float_app_scale) {
                val content = it.findViewById<RelativeLayout>(R.id.rlContent)
                val iv = it.findViewById<ImageView>(R.id.ivContent)
                iv.setImageURI(imageUri)
                val params = content.layoutParams as FrameLayout.LayoutParams
                it.findViewById<ScaleImage>(R.id.ivScale).onScaledListener =
                    object : ScaleImage.OnScaledListener {
                        override fun onScaled(x: Float, y: Float, event: MotionEvent) {
                            params.width = kotlin.math.max(params.width + x.toInt(), 400)
                            params.height = kotlin.math.max(params.height + y.toInt(), 300)
                            // 更新xml根布局的大小
//                            content.layoutParams = params
                            // 更新悬浮窗的大小，可以避免在其他应用横屏时，宽度受限
                            EasyFloat.updateFloat(
                                FLOAT_IMAGE_TAG,
                                width = params.width,
                                height = params.height
                            )
                        }
                    }

                it.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
                    EasyFloat.dismiss(FLOAT_IMAGE_TAG)
                }
            }
            .show()
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
        val view = EasyFloat.getFloatView(FLOAT_TEXT_TAG)
        view?.findViewById<TextView>(R.id.floatingText)?.textSize = Options.mTextSize.toFloat()
        binding.homeTextSizeTv.text = Options.mTextSize.toString()
    }

    override fun onTextChange() {
        val view = EasyFloat.getFloatView(FLOAT_TEXT_TAG)
        view?.findViewById<TextView>(R.id.floatingText)?.text = Options.mText
    }

    override fun onTextColorChange() {
        val view = EasyFloat.getFloatView(FLOAT_TEXT_TAG)
        view?.findViewById<TextView>(R.id.floatingText)?.setTextColor(Options.mTextColor)
        binding.homeTextColor.setBackgroundColor(Options.mTextColor)
    }

    override fun onBackgroundChange() {
        val view = EasyFloat.getFloatView(FLOAT_TEXT_TAG)
        view?.setBackgroundColor(Options.mBackgroundColor)
        binding.homeBackgroundColor.setBackgroundColor(Options.mBackgroundColor)
    }
}