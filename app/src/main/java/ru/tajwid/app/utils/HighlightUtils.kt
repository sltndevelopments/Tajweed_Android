package ru.tajwid.app.utils


/**
 * Created by BArtWell on 21.10.2018.
 */
object HighlightUtils {

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