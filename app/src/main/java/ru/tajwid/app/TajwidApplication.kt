package ru.tajwid.app

import android.app.Application
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import ru.tajwid.app.content.db.DbManager
import ru.tajwid.app.receiver.NotificationsAlarmReceiver
import ru.tajwid.app.utils.JsonImportHelper
import ru.tajwid.app.utils.NotificationsHelper
import ru.tajwid.app.utils.PreferencesHelper
import java.util.*


class TajwidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferencesHelper.init(this)
        DbManager.init(applicationContext)
        Firebase.analytics.setAnalyticsCollectionEnabled(true)
        JsonImportHelper.import(this)
        scheduleReminder()
        checkUpdates()
    }

    private fun checkUpdates() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            fetchAndActivate().addOnCompleteListener {
                val playStoreVersionCode = getLong(KEY_FB_VERSION_CODE)
                val botId = getString(KEY_TELEGRAM_BOT_ID)
                val chatId = getString(KEY_TELEGRAM_CHAT_ID)

                PreferencesHelper.get().saveBotId(botId)
                PreferencesHelper.get().saveChatId(chatId)

                if (BuildConfig.VERSION_CODE < playStoreVersionCode
                    && PreferencesHelper.get().isNotificationsEnabled()
                ) {
                    NotificationsHelper.showCheckUpdatesNotification(
                        this@TajwidApplication,
                        getString(R.string.title_new_app_version),
                        getString(R.string.desc_new_app_version),
                    )
                }
            }
        }
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

    companion object {
        private const val KEY_FB_VERSION_CODE = "android_version_code"
        private const val KEY_TELEGRAM_BOT_ID = "telegram_bot"
        private const val KEY_TELEGRAM_CHAT_ID = "telegram_channel"
    }
}