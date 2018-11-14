package ru.tajwid.app.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan

/**
 * Created by abadretdinov
 * on 14.11.2018
 */
class ArabicHighlighter(private val text: CharSequence) {
    private val sizeMultiplier = 1.6f
    fun getHighlighted(typeface: Typeface?): CharSequence {
        val listOfArabics = ArabicFinder(text).find()
        val highlightedStringBuilder = SpannableStringBuilder(text)
        for (pair in listOfArabics) {
            typeface?.let {
                highlightedStringBuilder.setSpan(StyleSpan(it.style), pair.start, pair.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            highlightedStringBuilder.setSpan(RelativeSizeSpan(sizeMultiplier), pair.start, pair.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return highlightedStringBuilder
    }
}