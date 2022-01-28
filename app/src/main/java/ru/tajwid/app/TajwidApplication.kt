package ru.tajwid.app

import android.app.Application
import ru.tajwid.app.content.db.DbManager
import ru.tajwid.app.receiver.NotificationsAlarmReceiver
import ru.tajwid.app.utils.JsonImportHelper
import ru.tajwid.app.utils.PreferencesHelper
import java.util.*

class TajwidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferencesHelper.init(this)
        DbManager.init(applicationContext)
        JsonImportHelper.import(this)
        scheduleReminder()
    }

    private fun scheduleReminder() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        calendar.set(Calendar.HOUR_OF_DAY, 19)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        PreferencesHelper.get().setNextNotificationTime(calendar.timeInMillis)
        NotificationsAlarmReceiver.schedule(this)
    }

    override fun onTerminate() {
        DbManager.destroy()
        super.onTerminate()
    }
}