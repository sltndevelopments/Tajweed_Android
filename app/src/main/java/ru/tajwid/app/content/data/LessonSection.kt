package ru.tajwid.app.content.data

import io.realm.RealmList
import io.realm.RealmObject

open class LessonSection : RealmObject() {
    var title: String = ""
    var arabicTitle: String? = ""
    var cards: RealmList<LessonCard> = RealmList()
}
