package ru.tajwid.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.tajwid.app.R
import ru.tajwid.app.content.data.Schedule
import ru.tajwid.app.ui.adapter.TimeZoneAdapter.MyViewHolder

class TimeZoneAdapter : RecyclerView.Adapter<MyViewHolder>() {

    private var list: List<Schedule> = emptyList()
    private var price: String = ""

    fun setData(list: List<Schedule>, price: String) {
        this.list = list
        this.price = price
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_timelearning, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val timeZoneLearning = list[position]
        holder.bind(timeZoneLearning)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teacher: TextView by lazy { itemView.findViewById(R.id.teacher) }
        val time: TextView by lazy { itemView.findViewById(R.id.time) }
        val weekDay: TextView by lazy { itemView.findViewById(R.id.week_day) }
        val priceText: TextView by lazy { itemView.findViewById(R.id.price) }

        fun bind(schedule: Schedule) {
            teacher.text =
                itemView.context.getString(R.string.title_item_teacher_name, schedule.teacher)
            time.text = schedule.time
            weekDay.text = schedule.day
            priceText.text = itemView.context.getString(R.string.price_placeholder, price)
            priceText.isVisible = price.isNotBlank()
        }
    }
}