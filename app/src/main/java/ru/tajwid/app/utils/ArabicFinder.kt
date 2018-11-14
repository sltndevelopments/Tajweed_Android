package ru.tajwid.app.utils

/**
 * Created by abadretdinov
 * on 14.11.2018
 */
class ArabicFinder(text: CharSequence) : Finder<List<ArabicFinder.ArabicFinderPosition>>(text) {

    companion object {
        const val starterArabicChar = 0x0600.toChar()
        const val endingArabicChar = 0x06FF.toChar()
    }

    override fun find(): List<ArabicFinderPosition> {
        val listOfPositions = mutableListOf<ArabicFinderPosition>()
        var starterPosition: Int? = null
        var endingPosition: Int? = null
        for (i: Int in 0 until text.length) {
            val c = text[i]
            if (c in starterArabicChar..endingArabicChar) {
                starterPosition?.let {
                    endingPosition = i + 1
                } ?: run {
                    starterPosition = i
                    endingPosition = i + 1
                }
            } else {
                starterPosition?.let {
                    listOfPositions.add(ArabicFinderPosition(it, endingPosition!!))

                    starterPosition = null
                    endingPosition = null
                }
            }
        }
        starterPosition?.let {
            listOfPositions.add(ArabicFinderPosition(it, endingPosition!!))
            starterPosition = null
            endingPosition = null
        }
        return listOfPositions
    }

    open class ArabicFinderPosition(start: Int, end: Int) : Finder.FinderPosition(start, end)
}