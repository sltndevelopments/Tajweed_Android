package ru.tajwid.app.utils

import android.view.View

class ViewStateHelper<in V : View>(private val callback: ViewStateCallback<V>) {

    private val views: ArrayList<V> = ArrayList()

    fun add(view: V) {
        views.add(view)
    }

    fun setEnabled(position: Int) {
        for (i in 0 until views.size) {
            callback.handleViewState(views[i], i == position)
        }
    }

    interface ViewStateCallback<in V> {
        fun handleViewState(view: V, isEnabled: Boolean)
    }
}