package ru.tajwid.app.ui.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import ru.tajwid.app.R

class ExerciseVariantsTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setTextColor(ContextCompat.getColor(context, R.color.blueberry))
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.text_size_20sp))
//        FontUtils.setTextViewFont(this, FontUtils.ARABIC_FONT)
        val padding = context.resources.getDimension(R.dimen.dimen_8dp).toInt()
        setPadding(padding, padding, padding, padding)
        gravity = Gravity.CENTER
        background = ContextCompat.getDrawable(context, R.drawable.exercise_border_black)
    }

    fun setUndone() {
        setTextColor(ContextCompat.getColor(this.context!!, R.color.red))
        background = ContextCompat.getDrawable(context, R.drawable.exercise_border_red)
    }

    fun setDone() {
        setTextColor(ContextCompat.getColor(this.context!!, R.color.shamrock_green))
        background = ContextCompat.getDrawable(context, R.drawable.exercise_border_green)
    }
}