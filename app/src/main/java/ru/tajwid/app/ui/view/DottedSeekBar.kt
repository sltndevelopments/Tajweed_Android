package ru.tajwid.app.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import ru.tajwid.app.R

class DottedSeekBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val seekBar = SeekBar(context)
    private val dotsContainer = LinearLayout(context)
    private val circleViewSize = resources.getDimension(R.dimen.dotted_seek_bar_circle_size).toInt()

    init {
        addView(dotsContainer)
        val dotsContainerLayoutParams = dotsContainer.layoutParams as MarginLayoutParams
        dotsContainerLayoutParams.width = MATCH_PARENT
        dotsContainerLayoutParams.height = MATCH_PARENT
        dotsContainerLayoutParams.leftMargin = seekBar.paddingLeft - seekBar.thumbOffset / 2
        dotsContainerLayoutParams.rightMargin = seekBar.paddingRight - seekBar.thumbOffset / 2
        dotsContainer.layoutParams = dotsContainerLayoutParams
        dotsContainer.orientation = LinearLayout.HORIZONTAL
        dotsContainer.gravity = Gravity.CENTER_VERTICAL

        addView(seekBar)
        val seekBarLayoutParams = seekBar.layoutParams
        seekBarLayoutParams.width = MATCH_PARENT
        seekBarLayoutParams.height = WRAP_CONTENT
        seekBar.layoutParams = seekBarLayoutParams
    }

    fun setOnSeekBarChangeListener(listener: SeekBar.OnSeekBarChangeListener) {
        seekBar.setOnSeekBarChangeListener(listener)
    }

    fun setMax(max: Int) {
        seekBar.max = max
        for (i in 1 until dotsContainer.childCount) {
            dotsContainer.removeView(getChildAt(i))
        }
        addCircleView(true)
        for (i in 0 until max) {
            addCircleView(false)
        }
    }

    private fun addCircleView(isFirstItem: Boolean) {
        if (!isFirstItem) {
            val marginView = View(context)
            dotsContainer.addView(marginView)
            val viewLayoutParams = marginView.layoutParams as LinearLayout.LayoutParams
            viewLayoutParams.width = 0
            viewLayoutParams.height = MATCH_PARENT
            viewLayoutParams.weight = 1f
        }

        val view = View(context)
        dotsContainer.addView(view)
        val viewLayoutParams = view.layoutParams as LinearLayout.LayoutParams
        viewLayoutParams.width = circleViewSize
        viewLayoutParams.height = circleViewSize
        view.layoutParams = viewLayoutParams
        view.background = ContextCompat.getDrawable(context, R.drawable.dotted_seek_bar_circle_background)
    }
}