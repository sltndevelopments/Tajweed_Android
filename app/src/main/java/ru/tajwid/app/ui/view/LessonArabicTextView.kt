package ru.tajwid.app.ui.view

import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import ru.tajwid.app.R

class LessonArabicTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setTextColor(ContextCompat.getColor(context, R.color.blueberry))
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.text_size_40sp))
//        FontUtils.setTextViewFont(this, FontUtils.ARABIC_FONT)
        val paddingTop = context.resources.getDimension(R.dimen.dimen_4dp).toInt()
        val paddingBottom = context.resources.getDimension(R.dimen.dimen_12dp).toInt()
        val paddingRight = context.resources.getDimension(R.dimen.dimen_24dp).toInt()
        setPadding(0, paddingTop, paddingRight, paddingBottom)
        //typeface = ResourcesCompat.getFont(context, R.font.droid_naskh_regular/*R.font.uthman*/)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }
}