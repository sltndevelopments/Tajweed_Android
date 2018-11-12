package ru.tajwid.app.ui.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_lesson_section_header.view.*
import ru.tajwid.app.R
import ru.tajwid.app.utils.FontUtils

class LessonSectionHeaderView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_lesson_section_header, this)

    }

    fun setLetter(letter: String) {
        lesson_section_header_letter.text = letter
//        FontUtils.setTextViewFont(lesson_section_header_title, FontUtils.ARABIC_FONT)
        if (!TextUtils.isEmpty(letter)) {
            lesson_section_header_letter.visibility = VISIBLE
        } else {
            lesson_section_header_letter.visibility = GONE
        }
    }

    fun setTitle(title: String) {
        lesson_section_header_title.text = title
        FontUtils.setTextViewFont(lesson_section_header_title, R.font.avenir_next_bold)

    }
}
