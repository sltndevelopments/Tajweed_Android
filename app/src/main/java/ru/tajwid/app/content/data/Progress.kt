package ru.tajwid.app.content.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Progress : RealmObject(), AutoIncrementIdentify {
    @PrimaryKey
    var id = 0
    var module = NO_VALUE
    var lesson = NO_VALUE
    var section = NO_VALUE
    var card = NO_VALUE
    var exercise = NO_VALUE
    var isCompleted = false

    companion object {
        const val NO_VALUE = -1
    }

    override fun setAutoIncrementId(autoIncrementId: Int) {
        id = autoIncrementId
    }
}