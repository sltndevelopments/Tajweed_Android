package ru.tajwid.app.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_circle_check.view.*
import ru.tajwid.app.R

class CircleCheckView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_circle_check, this)
    }

    fun setDone(isDone: Boolean) {
        circle_check_view_image.setImageResource(if (isDone) R.drawable.ic_circle_check_green else R.drawable.ic_circle_check_grey)
    }
}
