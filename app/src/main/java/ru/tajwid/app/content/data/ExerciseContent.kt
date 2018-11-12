package ru.tajwid.app.content.data

import android.os.Parcelable
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.WriteWith
import ru.tajwid.app.content.StringRealmListParceler

@Parcelize
@RealmClass
open class ExerciseContent(
        var title: String,
        var variants: @WriteWith<StringRealmListParceler> RealmList<String> = RealmList(),
        var correctVariant: String = "",
        var rows: @WriteWith<StringRealmListParceler> RealmList<String> = RealmList(),
        var example: String = "",
        var transcription: String = "",
        var correctWriting: String = "",
        var text: String = "",
        var correctWords: @WriteWith<StringRealmListParceler> RealmList<String> = RealmList()
) : RealmObject(), Parcelable {

    constructor() : this("")
}
