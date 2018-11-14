package ru.tajwid.app.utils

/**
 * Created by abadretdinov
 * on 14.11.2018
 */
abstract class Finder<T>(protected val text: CharSequence) {
    abstract fun find(): T

    open class FinderPosition(
            val start: Int,
            val end: Int
    )
}