package ru.tajwid.app.utils.highlight

import android.os.Parcel
import android.text.ParcelableSpan
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

/**
 * Created by abadretdinov
 * on 14.11.2018
 */
class BaselineRelativeSizeSpan(
        private val proportion: Float
) : MetricAffectingSpan(), ParcelableSpan {
    override fun getSpanTypeId(): Int {
        return 3
    }

    constructor(src: Parcel) : this(src.readFloat())

    override fun updateMeasureState(p: TextPaint) {
        updateAnyState(p)
    }

    override fun updateDrawState(tp: TextPaint) {
        updateAnyState(tp)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeFloat(proportion)
    }

    fun getSizeChange(): Float {
        return proportion
    }

    override fun describeContents(): Int {
        return 0
    }

    private fun updateAnyState(paint: TextPaint) {
        val ascent = paint.ascent()

        paint.textSize *= proportion

        val newAscent = paint.ascent()
        paint.baselineShift += ((ascent - newAscent) / 2).toInt()
    }
}