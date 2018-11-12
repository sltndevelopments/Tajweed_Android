package ru.tajwid.app.utils

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView


/**
 * Created by BArtWell on 21.10.2018.
 */
object HighlightUtils {
    fun getSpannablePositions(text: String): Pair<String, List<Triple<Int, Int, Int>>> {
        val tags = listOf("<:", "<;", ":>", ";>")

        val listOfSpannable = mutableListOf<Triple<Int, Int, Int>>()

        var taglessText = text

        var currentColor = Color.TRANSPARENT
        var currentStart = 0
        var currentEnd: Int
        var currentTags = tags
        while ( //если тэги есть, мы все еще их удаляем
                tags.any {
                    taglessText.contains(it)
                }) {
            /*
            нужный нам вариант - только подходящие нам тэги
            к примеру, если мы до этого нашли тэг <:, то нет смысла искать тэг ;>,
            а если мы нашли ;>, значит мы начинаем поиски заново и ищем любые тэги
             */
            val result = taglessText.findAnyOf(currentTags)
            result?.let { spannableResult ->
                /*
                * в зависимости от того, что нам вернул поиск, мы выбираем, как дальше быть.
                * начинать ли новый Spannable, в случаях <: и <;,
                * либо закрывать текущий, если не нашли подходящего, либо закрывающий тэг стоит после открывающего,
                * либо просто удалить проверяемый, если это подходящий, но раньше открывающего
                * */
                when (spannableResult.second) {
                    "<:" -> {
                        currentColor = Color.parseColor("#1fade3")
                        currentStart = spannableResult.first
                        currentTags = listOf(":>")
                    }
                    ":>" -> {
                        currentEnd = spannableResult.first
                        if (currentStart < currentEnd) {
                            listOfSpannable.add(Triple(currentStart, currentEnd, currentColor))
                            currentTags = tags
                        }
                    }
                    "<;" -> {
                        currentColor = Color.parseColor("#e92530")
                        currentStart = spannableResult.first
                        currentTags = listOf(";>")
                    }
                    ";>" -> {
                        currentEnd = spannableResult.first
                        if (currentEnd > currentStart) {
                            listOfSpannable.add(Triple(currentStart, currentEnd, currentColor))
                            currentTags = tags
                        }
                    }
                }
                taglessText = taglessText.removeRange(spannableResult.first, spannableResult.first + spannableResult.second.length)
            } ?: run {
                currentColor = Color.TRANSPARENT
                currentStart = 0
                currentEnd = 0
                currentTags = tags
            }
        }
        return Pair(taglessText, listOfSpannable)
    }

    fun setHighlightedText(view: TextView, text: String) {
        val spannables = getSpannablePositions(/*ArabicUtilities.reshape(*/text/*)*/)

        val spannedText = SpannableStringBuilder(spannables.first)
        /*if(true) {
            view.text = spannedText
            return
        }*/
        val flag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE

        // нормальный цвет, которым окрашиваем весь невыделенный текст
        var normalColor = view.currentTextColor
        // храним случайно окрашенный символ, который нужно перекрасить в нормальный цвет
        var lastFalslyColored = 0

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

        spannables.second.forEach {
            if (lastFalslyColored != it.first && it.first > 0) {
                spannedText.setSpan(ForegroundColorSpan(normalColor), it.first - 1, it.first, flag)
            }
            if (lastFalslyColored < it.first) {
                spannedText.setSpan(ForegroundColorSpan(normalColor), lastFalslyColored, lastFalslyColored + 1, flag)
            }
            spannedText.setSpan(ForegroundColorSpan(it.third), it.first, it.second, flag)
            lastFalslyColored = it.second
        }
        if (lastFalslyColored in 1 until spannedText.length) {
            spannedText.setSpan(ForegroundColorSpan(normalColor), lastFalslyColored, lastFalslyColored + 1, flag)
        }
        view.setText(spannedText, TextView.BufferType.SPANNABLE)
    }

    private fun findPositions(text: String, open: String, close: String): MutableList<Pair<Int, Int>> {
        val positions = mutableListOf<Pair<Int, Int>>()
        var previous = 0
        var shouldContinue = true
        while (shouldContinue) {
            val start = text.indexOf(open, previous)
            if (start == -1) {
                shouldContinue = false
            } else {
                val end = text.indexOf(close, start)
                if (end == -1) {
                    shouldContinue = false
                } else {
                    previous = end
                    positions.add(Pair(start, end))
                }
            }
        }
        return positions
    }
}