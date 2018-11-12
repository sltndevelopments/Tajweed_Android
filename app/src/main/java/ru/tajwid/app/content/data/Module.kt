package ru.tajwid.app.content.data

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Module : RealmObject(), AutoIncrementIdentify {
    @PrimaryKey
    var id = 0
    var title: String = ""
    var lessons: RealmList<Lesson> = RealmList()

    override fun setAutoIncrementId(autoIncrementId: Int) {
        id = autoIncrementId
    }
}