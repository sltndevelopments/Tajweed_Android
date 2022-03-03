package ru.tajwid.app.content.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OnlineLearning(
    val groups: List<Group> = emptyList()
) : Parcelable


@Parcelize
data class Group(
    val name: String = "",
    val link: String = "",
    val schedule: List<Schedule> = emptyList()
) : Parcelable

@Parcelize
data class Schedule(
    val time: String = "",
    val day: String = "",
    val teacher: String = "",
) : Parcelable