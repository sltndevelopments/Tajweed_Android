package ru.tajwid.app.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import ru.tajwid.app.R
import ru.tajwid.app.ui.activity.MainActivity

private const val EXTRA_NOTIFICATION = "notification"
private const val NOTIFICATION_ID = 102
private const val CHECK_UPDATES_NOTIFICATION_ID = 103
private const val NOTIFICATION_REQUEST_CODE = 1
private const val CHECK_UPDATES_REQUEST_CODE = 2
private const val CHANNEL_ID = "Tajwid_Notifications_Channel"

object NotificationsHelper {







    fun showNotification(
        context: Context,
        title: String,
        text: String
    ) {
        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_NOTIFICATION, true)
        }

        showNotificationInner(
            context, title, text,
            NOTIFICATION_ID,
            getPendingIntent(
                context,
                NOTIFICATION_REQUEST_CODE,
                notificationIntent
            )
        )
    }

    private fun showNotificationInner(
        context: Context,
        title: String,
        text: String,
        notificationId: Int,
        pendingIntent: PendingIntent
    ) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setChannelId(CHANNEL_ID)
            .setPriority(Notification.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_ALL)
            .build()
        val notificationManager = getNotificationManager(context)
        notificationManager.notify(notificationId, notification)
    }

    fun showCheckUpdatesNotification(
        context: Context,
        title: String,
        text: String,
    ) {
        val appPackageName = context.packageName

        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")
            ).let {
                if (it.resolveActivity(context.packageManager) == null) {
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                } else it
            }

        showNotificationInner(
            context, title, text,
            CHECK_UPDATES_NOTIFICATION_ID,
            getPendingIntent(context, CHECK_UPDATES_REQUEST_CODE, intent)
        )
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = CHANNEL_ID
            val name = context.getString(R.string.app_name)
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            channel.description = context.getString(R.string.notification_channel_description)
            channel.enableLights(true)
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }

    private fun getPendingIntent(
        context: Context,
        requestCode: Int,
        intent: Intent
    ): PendingIntent {
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun cancelNotification(context: Context?) {
        if (context != null) {
            getNotificationManager(context).cancel(NOTIFICATION_ID)
        }
    }
}