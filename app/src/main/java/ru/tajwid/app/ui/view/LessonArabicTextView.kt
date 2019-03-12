package ru.tajwid.app.ui.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import ru.tajwid.app.R
import ru.tajwid.app.utils.FontUtils

class LessonArabicTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setTextColor(ContextCompat.getColor(context, R.color.blueberry))
        gravity = Gravity.RIGHT
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.text_size_48sp))
//        FontUtils.setTextViewFont(this, FontUtils.ARABIC_FONT)
        val paddingTop = context.resources.getDimension(R.dimen.dimen_4dp).toInt()
        val paddingBottom = context.resources.getDimension(R.dimen.dimen_12dp).toInt()
        val paddingRight = context.resources.getDimension(R.dimen.dimen_24dp).toInt()
        val paddingLeft = context.resources.getDimension(R.dimen.dimen_4dp).toInt()
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        typeface = ResourcesCompat.getFont(context, FontUtils.getArabicTypefaceResId())
    }

}