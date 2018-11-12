package ru.tajwid.app.content.data

import io.realm.RealmList
import io.realm.RealmObject

open class LessonCard : RealmObject() {
    var title: String = ""
    var contentItems: RealmList<LessonCardContentItem> = RealmList()
}