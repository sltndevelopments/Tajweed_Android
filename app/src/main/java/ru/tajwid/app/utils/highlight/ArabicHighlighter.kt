package ru.tajwid.app.utils.highlight

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan

/**
 * Created by abadretdinov
 * on 14.11.2018
 */
class ArabicHighlighter(private val text: CharSequence) {
    private val sizeMultiplier = 2f
    fun getHighlighted(typeface: Typeface?): CharSequence {
        val listOfArabics = ArabicFinder(text).find()
        val highlightedStringBuilder = SpannableStringBuilder(text)
        for (pair in listOfArabics) {
            typeface?.let {
                highlightedStringBuilder.setSpan(TypefaceSpanCompat(it), pair.start, pair.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            highlightedStringBuilder.setSpan(/*BaselineRelativeSizeSpan*/RelativeSizeSpan(sizeMultiplier), pair.start, pair.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return highlightedStringBuilder
    }
}