package com.zfh.teleprompter.activity.mvp.setting.history

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.zfh.teleprompter.Options
import com.zfh.teleprompter.R
import com.zfh.teleprompter.activity.BaseActivity
import com.zfh.teleprompter.activity.mvp.setting.history.adapter.HistoryAdapter
import com.zfh.teleprompter.app.App
import com.zfh.teleprompter.databinding.ActivityHistoryBinding
import com.zfh.teleprompter.db.DBHelper
import com.zfh.teleprompter.db.entry.FloatText
import com.zfh.teleprompter.event.Event
import org.greenrobot.eventbus.EventBus

class HistoryActivity : BaseActivity() {

    companion object {
        private const val TAG = "HistoryActivity"

        fun start(context: Context) {
            val intent = Intent(context, HistoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
        }
    }

    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }
    private var dialog : Dialog? = null
    @SuppressLint("MissingInflatedId")
    private val onClick: (FloatText) -> Unit = { floatText ->
        dialog = Dialog(this).apply {
            setContentView(R.layout.dialog_history_detail)
            window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent)
                val params = attributes.apply {
                    width = WindowManager.LayoutParams.WRAP_CONTENT
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                    gravity = Gravity.CENTER
                }
                attributes = params
            }
            setCancelable(true)
            setCanceledOnTouchOutside(true)

            val msgView = findViewById<TextView>(R.id.dialog_history_msg)
            msgView.text = floatText.text
            msgView.movementMethod = ScrollingMovementMethod.getInstance()

            findViewById<View>(R.id.dialog_history_delete).setOnClickListener {
                DBHelper.delete(floatText.text)
                dismiss()
            }

            findViewById<View>(R.id.dialog_history_save).setOnClickListener {
                Options.mText = floatText.text
                EventBus.getDefault().post(Event.onTextChange())
                dismiss()
            }
        }
        dialog?.show()
    }

    private var historyAdapter = HistoryAdapter(onClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleText("历史记录")
        setContentLayoutView(binding.root)

        binding.historyList.layoutManager = LinearLayoutManager(this)
        binding.historyList.adapter = historyAdapter

        val historyLiveData = DBHelper.getAllFloatText()
        historyLiveData.observe(this) { list ->
            historyAdapter.update(list)
        }

    }


}