package ru.tajwid.app.content.data

import io.realm.RealmList
import io.realm.RealmObject

open class Lesson : RealmObject() {
    var title: String = ""
    var sections: RealmList<LessonSection> = RealmList()
    var exercises: RealmList<Exercise> = RealmList()
}