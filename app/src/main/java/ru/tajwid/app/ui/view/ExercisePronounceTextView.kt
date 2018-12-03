package ru.tajwid.app.ui.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import ru.tajwid.app.R
import ru.tajwid.app.utils.FontUtils

class ExercisePronounceTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setTextColor(ContextCompat.getColor(context, R.color.blueberry))
//        FontUtils.setTextViewFont(this, FontUtils.ARABIC_FONT)

        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.text_size_48sp))
        val paddingTop = context.resources.getDimension(R.dimen.dimen_24dp).toInt()
        setPadding(0, paddingTop, 0, 0)
        typeface = ResourcesCompat.getFont(context, FontUtils.getArabicTypefaceResId())

        /*addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                var text = s.toString()
                var changed:Boolean
                do {
                    changed = false
                    var totalCurrentLength=0
                    val lines = text.lines()
                    for (i in 0 until lines.size) {
                        val line=lines[i]
                        val lineLength=line.length
                        val changedLength=getWrappedStringLength(line)
                        if(changedLength!=lineLength){
                            text=text.replaceRange(changedLength,changedLength,"\n")
                            changed=true
                        }
                        totalCurrentLength+=lineLength
                    }
                } while (changed)
                if(text!=s.toString()){
                    setText(text)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })*/
    }
/*
    private fun getWrappedStringLength(line: String): Int {
        val maxWidth = measuredWidth - paddingLeft - paddingRight
        if(maxWidth<=0){
            return line.length
        }

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
        if(currentLine.length<=1){
            return line.length
        }
        return currentLine.length
    }*/

}