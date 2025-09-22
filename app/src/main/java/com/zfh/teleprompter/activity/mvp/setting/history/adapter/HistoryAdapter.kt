package com.zfh.teleprompter.activity.mvp.setting.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zfh.teleprompter.R
import com.zfh.teleprompter.db.entry.FloatText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(val onClick: (FloatText) -> Unit) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private val data: MutableList<FloatText> = mutableListOf()

    fun update(list: List<FloatText>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_history, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.text.text = data[position].text
        viewHolder.time.text = getTime(data[position].createTime)
        viewHolder.itemView.setOnClickListener { onClick(data[position]) }
    }

    override fun getItemCount() = data.size

    fun getTime(timestamp: Long): String {
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(date)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.item_history_text)
        val time: TextView = view.findViewById(R.id.item_history_time)
    }

}