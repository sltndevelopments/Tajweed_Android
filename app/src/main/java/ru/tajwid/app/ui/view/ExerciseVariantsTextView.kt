package ru.tajwid.app.ui.view

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
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

        setBreakRules()

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                var changed = false
                var text = s.toString()
                do {
                    var lines = text.lines()
                    for (i in 0 until lines.size) {

                    }

                } while (changed)


                s.toString().lines()

                paint.measureText(s.toString())
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun getWrappedStringLength(line: String): Int {
        val maxWidth = measuredWidth - paddingLeft - paddingRight

        var currentLine = line
        val rect = Rect()
        paint.getTextBounds(currentLine, 0, currentLine.length, rect)
        var width = rect.width()

        while (width > maxWidth && currentLine.length > 1) {
            var isSpace = true
            while (!isSpace && currentLine.length > 1) {
                currentLine = currentLine.substring(IntRange(0, currentLine.length - 1))
                isSpace = ' ' == line[currentLine.length]
            }
            paint.getTextBounds(currentLine, 0, currentLine.length, rect)
            width = rect.width()
        }
        return currentLine.length
    }

    private fun setBreakRules() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            breakStrategy = Layout.BREAK_STRATEGY_HIGH_QUALITY
            hyphenationFrequency = Layout.HYPHENATION_FREQUENCY_NONE
        }
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