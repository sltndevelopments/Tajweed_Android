package ru.tajwid.app.utils

import android.support.annotation.FontRes
import android.support.v4.content.res.ResourcesCompat
import android.widget.TextView

//private const val FONT_PATH = "fonts/"

class FontUtils {
    companion object {
        /*    const val ARABIC_FONT = "simpo.ttf"
            const val MONTSERRAT_REGULAR = "montserrat_regular.ttf"
            const val FONT_AVENIR_NEXT_MEDIUM = "avenir_next_medium.ttf"
            const val FONT_AVENIR_NEXT_BOLD = "avenir_next_bold.ttf"
            const val FONT_PROXIMA_NOVA_BOLD = "proxima_nova_bold.ttf"
            const val FONT_PROXIMA_NOVA_REGULAR = "proxima_nova_regular.ttf"
            const val FONT_PROXIMA_NOVA_SEMIBOLD = "proxima_nova_semibold.ttf"*/

        fun setTextViewFont(textView: TextView, @FontRes fontResId: Int) {
            textView.typeface = ResourcesCompat.getFont(textView.context, fontResId)
            /*val typeface = Typeface.createFromAsset(textView.context.assets, FONT_PATH + font)
            textView.typeface = typeface*/
        }
    }
}