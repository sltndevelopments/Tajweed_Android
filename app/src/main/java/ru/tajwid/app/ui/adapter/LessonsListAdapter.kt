package ru.tajwid.app.ui.adapter

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import ru.tajwid.app.content.data.Lesson
import ru.tajwid.app.ui.adapter.holder.LessonsListHolder
import ru.tajwid.app.ui.view.LessonsListItemView

/**
 * Created by BArtWell on 17.02.2018.
 */
class LessonsListAdapter : BaseRecyclerAdapter<LessonsListHolder>() {

    var items = mutableListOf<Item>()
    var listener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsListHolder {
        val view = LessonsListItemView(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        return LessonsListHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: LessonsListHolder, position: Int) {
        holder.bind(items[position].lesson, items[position].isFilled, listener)
    }

    data class Item(val lesson: Lesson, val isFilled: Boolean)
    interface OnClickListener {
        fun onClick(adapterPosition: Int)
    }
}