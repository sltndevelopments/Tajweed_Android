package ru.tajwid.app.utils

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan

/**
 * Created by abadretdinov
 * on 14.11.2018
 */
class TagHighlighter(private val text: CharSequence) {
    /**
     * @param normalColor нормальный цвет, которым окрашиваем весь невыделенный текст
     * */
    fun getHighlighted(normalColor: Int): CharSequence {
        val pairOfTaggedPositions = TagFinder(text).find()

        val spannedText = SpannableStringBuilder(pairOfTaggedPositions.first)
        /*if(true) {
            view.text = spannedText
            return
        }*/
        val flag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE

        // храним случайно окрашенный символ, который нужно перекрасить в нормальный цвет
        var lastFalselyColored = 0

        /*if(spannables.second.isNotEmpty()){
          spannedText.forEachIndexed { index, _ ->
              spannedText.setSpan(BackgroundColorSpan(Color.TRANSPARENT),index,index+1,flag)
          }
      }*/

        /*
        * тут идут 2 хака, чтобы vowels (короче закорючки над символами) окрашивались отдельно от остальных букв
        * ибо в обычной ситуации android отказывается их красить отдельно, что бы я ни делал.
        * хак 1 - мы окрашиваем символ, что идет до текущего (если он не был окрашен до этого) в нормальный цвет
        * хак 2 - мы окрашиваем символ, что шел после последнего окрашенного (если он не будет окрашен сейчас) в нормальный цвет
        * */

        pairOfTaggedPositions.second.forEach {
            if (lastFalselyColored != it.start && it.start > 0) {
                spannedText.setSpan(ForegroundColorSpan(normalColor), it.start - 1, it.start, flag)
            }
            if (lastFalselyColored < it.start) {
                spannedText.setSpan(ForegroundColorSpan(normalColor), lastFalselyColored, lastFalselyColored + 1, flag)
            }
            spannedText.setSpan(ForegroundColorSpan(it.color), it.start, it.end, flag)
            lastFalselyColored = it.end
        }
        if (lastFalselyColored in 1 until spannedText.length) {
            spannedText.setSpan(ForegroundColorSpan(normalColor), lastFalselyColored, lastFalselyColored + 1, flag)
        }
        return spannedText
    }
}