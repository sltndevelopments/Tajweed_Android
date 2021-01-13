package ru.tajwid.app.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import ru.tajwid.app.R
import ru.tajwid.app.utils.FontUtils

class LessonCardTitleTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setTextColor(ContextCompat.getColor(context, R.color.black))
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.text_size_24sp))
        FontUtils.setTextViewFont(this, FontUtils.getRegularTypefaceResId())

        val paddingTop = context.resources.getDimension(R.dimen.dimen_24dp).toInt()
        setPadding(0, paddingTop, 0, 0)
    }
}