package ru.tajwid.app.content.data

import io.realm.RealmList
import io.realm.RealmObject

open class LessonCardContentItem : RealmObject() {
    var type: String = ""
    var content: String = ""
    var value: RealmList<String> = RealmList()
}