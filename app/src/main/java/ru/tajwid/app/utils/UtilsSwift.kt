package ru.tajwid.app.utils

/**
 * Created by abadretdinov
 * on 26.10.2018
 */
object UtilsSwift {

    fun changeText(text: String) {
        var newText = text
                .replace("<:", "")
                .replace(":>", "")
                .replace("<:ً", "ً")
                .replace("<:ْ", "ْ")
                .replace("<:ٌ", "ٌ")
                .replace("<:ٍ", "ٍ")
                .replace("<:ًّ", "ً")
                .replace(":>ْ", "ْ")
                .replace("<:َ", "َ")
                .replace("<:ُ", "ُ")
                .replace(":>ِ", "ِ")
                .replace(":>َ", "َ")
                .replace(";>", "")
                .replace("<;ُ", "ُ")
                .replace("<;َ", "َ")
                .replace("<;ْ", "ْ")
                .replace("<;ِ", "ِ")
                .replace("<;", "")
                .replace(";>ِ", "ِ")
                .replace(";>َ", "َ")
                .replace(";>ُ", "ُ")
                .replace("<:ِ", "ِ")
                .replace("<;َّ", "َ")
                .replace("<;ِّ", "ِ")
    }
}