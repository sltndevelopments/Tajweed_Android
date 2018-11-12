package ru.tajwid.app.ui.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import ru.tajwid.app.R

class LessonPlainTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    init {
        setTextColor(ContextCompat.getColor(context, R.color.greyish_brown))
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.text_size_20sp))
        val paddingLeft = context.resources.getDimension(R.dimen.dimen_24dp).toInt()
        val paddingRight = context.resources.getDimension(R.dimen.dimen_24dp).toInt()
        val paddingTop = context.resources.getDimension(R.dimen.dimen_20dp).toInt()
        setPadding(paddingLeft, paddingTop, paddingRight, 0)
    }

//    override fun setText(text: CharSequence?, type: BufferType?) {
//
//        super.setText("_"+"َ ", type)
//    }
}