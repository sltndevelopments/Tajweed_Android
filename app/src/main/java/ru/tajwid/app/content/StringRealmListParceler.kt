package ru.tajwid.app.content

import android.os.Parcel
import io.realm.RealmList
import kotlinx.android.parcel.Parceler

/**
 * Created by BArtWell on 27.09.2018.
 */

object StringRealmListParceler : Parceler<RealmList<String>> {
    override fun create(parcel: Parcel): RealmList<String> {
        val list = RealmList<String>()
        parcel.readStringList(list)
        return list
    }

    override fun RealmList<String>.write(parcel: Parcel, flags: Int) {
        parcel.writeStringList(this)
    }
}