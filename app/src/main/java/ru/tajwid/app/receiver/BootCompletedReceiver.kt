package ru.tajwid.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            NotificationsAlarmReceiver.schedule(context)
        }
    }
}