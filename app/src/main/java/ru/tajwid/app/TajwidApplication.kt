package ru.tajwid.app

import android.app.Application
import ru.tajwid.app.content.db.DbManager
import ru.tajwid.app.utils.PreferencesHelper

class TajwidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferencesHelper.init(this)
        DbManager.init(applicationContext)
    }

    override fun onTerminate() {
        DbManager.destroy()
        super.onTerminate()
    }
}