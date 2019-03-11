package ru.tajwid.app.utils.highlight

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Parcel
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class TypefaceSpanCompat : MetricAffectingSpan {

    /**
     * Returns the font family name set in the span.
     *
     * @return the font family name
     * @see .TypefaceSpanCompat
     */
    private val family: String?
    /**
     * Returns the typeface set in the span.
     *
     * @return the typeface set
     * @see .TypefaceSpanCompat
     */
    private val typeface: Typeface?
    /** @hide
     */

    /**
     * Constructs a [TypefaceSpanCompat] based on the font family. The previous style of the
     * TextPaint is kept. If the font family is null, the text paint is not modified.
     *
     * @param family The font family for this typeface.  Examples include
     * "monospace", "serif", and "sans-serif"
     */
    constructor(family: String?) : this(family, null) {}

    /**
     * Constructs a [TypefaceSpanCompat] from a [Typeface]. The previous style of the
     * TextPaint is overridden and the style of the typeface is used.
     *
     * @param typeface the typeface
     */
    constructor(typeface: Typeface) : this(null, typeface) {}

    /**
     * Constructs a [TypefaceSpanCompat] from a  parcel.
     */
    constructor(src: Parcel) {
        family = src.readString()
        typeface = LeakyTypefaceStorage.readTypefaceFromParcel(src)
    }

    private constructor(family: String?, typeface: Typeface?) {
        this.family = family
        this.typeface = typeface
    }

    /** @hide
     */
    private fun writeToParcelInternal(dest: Parcel, flags: Int) {
        dest.writeString(family)
        LeakyTypefaceStorage.writeTypefaceToParcel(typeface, dest)
    }

    override fun updateDrawState(ds: TextPaint) {
        updateTypeface(ds)
    }

    override fun updateMeasureState(paint: TextPaint) {
        updateTypeface(paint)
    }

    private fun updateTypeface(paint: Paint) {
        if (typeface != null) {
            paint.typeface = typeface
        } else if (family != null) {
            applyFontFamily(paint, family)
        }
    }

    private fun applyFontFamily(paint: Paint, family: String) {
        val style: Int
        val old = paint.typeface
        style = old?.style ?: Typeface.NORMAL
        val styledTypeface = Typeface.create(family, style)
        val fake = style and styledTypeface.style.inv()
        if (fake and Typeface.BOLD !== 0) {
            paint.isFakeBoldText = true
        }
        if (fake and Typeface.ITALIC !== 0) {
            paint.textSkewX = -0.25f
        }
        paint.typeface = styledTypeface
    }
}