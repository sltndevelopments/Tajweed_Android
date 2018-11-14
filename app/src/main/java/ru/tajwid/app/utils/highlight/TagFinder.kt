package ru.tajwid.app.utils.highlight

import android.graphics.Color
import ru.tajwid.app.utils.Finder

/**
 * Created by abadretdinov
 * on 14.11.2018
 */
class TagFinder(text: CharSequence) : Finder<Pair<CharSequence, List<TagFinder.TagFinderPosition>>>(text) {
    companion object {
        val tags = listOf("<:", "<;", ":>", ";>")
    }

    override fun find(): Pair<CharSequence, List<TagFinderPosition>> {
        val listOfSpannable = mutableListOf<TagFinderPosition>()

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
                            listOfSpannable.add(TagFinderPosition(currentStart, currentEnd, currentColor))
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
                            listOfSpannable.add(TagFinderPosition(currentStart, currentEnd, currentColor))
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


    open class TagFinderPosition(
            start: Int,
            end: Int,
            val color: Int
    ) : FinderPosition(start, end)
}