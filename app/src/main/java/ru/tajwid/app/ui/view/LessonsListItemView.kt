package ru.tajwid.app.ui.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_lessons_list_item.view.*
import ru.tajwid.app.R
import ru.tajwid.app.utils.FontUtils

/**
 * Created by BArtWell on 17.02.2018.
 */
class LessonsListItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_lessons_list_item, this)
    }

    fun setNumber(number: Int, isFilled: Boolean) {
        lessons_list_item_number.text = number.toString()
        if (isFilled) {
            lessons_list_item_number.setTextColor(ContextCompat.getColor(context, R.color.white))
            lessons_list_item_number.background = ContextCompat.getDrawable(context, R.drawable.lessons_list_item_filled_background)
        } else {
            lessons_list_item_number.setTextColor(ContextCompat.getColor(context, R.color.black))
            lessons_list_item_number.background = ContextCompat.getDrawable(context, R.drawable.lessons_list_item_empty_background)
        }
    }

    fun setTitle(title: String) {
        lessons_list_item_title.text = title
        FontUtils.setTextViewFont(lessons_list_item_title, R.font.montserrat_regular)

    }

    fun setInfo(text: String) {
        lessons_list_item_info.text = text
        FontUtils.setTextViewFont(lessons_list_item_info, R.font.montserrat_regular)
    }
}