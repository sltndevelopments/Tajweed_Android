package ru.tajwid.app.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.util.Log
import ru.tajwid.app.R
import ru.tajwid.app.utils.NotificationsHelper
import ru.tajwid.app.utils.PreferencesHelper

private const val TAG = "NotificationsAlarmRecei"

class NotificationsAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(TAG, "Receive at " + DateFormat.format("dd/MM/yyyy HH:mm:ss", System.currentTimeMillis()))

        if (PreferencesHelper.get().isNotificationsEnabled()) {
            NotificationsHelper.showNotification(context, context.getString(R.string.app_name), context.getString(R.string.notification_channel_description))
        }
    }

    companion object {

        fun schedule(context: Context) {
            val nextExecutionTime = PreferencesHelper.get().getNextNotificationTime()
            val intent = Intent(context, NotificationsAlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextExecutionTime, pendingIntent)
            Log.d(TAG, "Scheduled on " + DateFormat.format("dd/MM/yyyy HH:mm:ss", nextExecutionTime))
        }
    }
}