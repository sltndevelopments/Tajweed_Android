package ru.tajwid.app.content.data

import android.os.Parcelable
import io.realm.RealmObject
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Exercise(
        var type: String,
        var content: ExerciseContent?
) : RealmObject(), Parcelable {

    constructor() : this("", null)
}