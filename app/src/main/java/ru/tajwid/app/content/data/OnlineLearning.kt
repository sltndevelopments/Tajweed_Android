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
/*
* {
  "groups": [
    {
      "name": "Мужская",
      "link": "https://app.proficonf.com/j/1ONGTflXcaz/",
      "schedule": [
        {
          "time":"Вторник, 17-00 по МСК"
        },
        {
          "time":"Четверг, 17-00 по МСК"
        }
      ]
    },
    {
      "name": "Женская",
      "link": "https://www.mail.ru",
      "schedule": [
        {
          "time":"Вторник, 17-00 по МСК"
        },
        {
          "time":"Четверг, 17-00 по МСК"
        }
      ]
    },
    {
      "name": "Детская",
      "link": "https://ya.ru",
      "schedule": [
        {
          "time":"Вторник, 17-00 по МСК"
        },
        {
          "time":"Четверг, 17-00 по МСК"
        }
      ]
    }
  ]
}*/