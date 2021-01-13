package ru.tajwid.app.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import ru.tajwid.app.R
import ru.tajwid.app.utils.FontUtils

class LessonHighlitedTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setTextColor(ContextCompat.getColor(context, R.color.greyish_brown))
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.text_size_20sp))
        FontUtils.setTextViewFont(this, R.font.avenir_next_medium)

        val paddingLeft = context.resources.getDimension(R.dimen.dimen_40dp).toInt()
        val paddingTop = context.resources.getDimension(R.dimen.dimen_20dp).toInt()
        setPadding(paddingLeft, paddingTop, 0, 0)
    }
}