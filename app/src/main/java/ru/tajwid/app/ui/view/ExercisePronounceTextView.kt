package ru.tajwid.app.ui.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import ru.tajwid.app.R
import ru.tajwid.app.utils.FontUtils

class ExercisePronounceTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    init {
        setTextColor(ContextCompat.getColor(context, R.color.blueberry))
//        FontUtils.setTextViewFont(this, FontUtils.ARABIC_FONT)

        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.text_size_40sp))
        val paddingTop = context.resources.getDimension(R.dimen.dimen_24dp).toInt()
        setPadding(0, paddingTop, 0, 0)
        typeface = ResourcesCompat.getFont(context, FontUtils.getArabicTypefaceResId())

    }
}