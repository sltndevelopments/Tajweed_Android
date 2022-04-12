package ru.tajwid.app.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

private const val IS_NOTIFICATIONS_ENABLED = "is_notifications_enabled"
private const val LAST_LAUNCH_TIME = "last_launch_time"
private const val BOT_ID = "telegram_bot"
private const val CHAT_ID = "telegram_chat"

class PreferencesHelper(context: Context) {
    private var sharedPreferences: SharedPreferences

    companion object {
        private var instance: PreferencesHelper? = null

        internal fun init(context: Context) {
            if (instance == null) {
                instance = PreferencesHelper(context)
            }
        }

        fun get(): PreferencesHelper {
            if (instance == null) {
                throw IllegalStateException("Configuration is not exist, call newInstance() first")
            }
            return instance as PreferencesHelper
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(IS_NOTIFICATIONS_ENABLED, enabled).apply()
    }

    fun isNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean(IS_NOTIFICATIONS_ENABLED, false)
    }

    fun setNextNotificationTime(enabled: Long) {
        sharedPreferences.edit().putLong(LAST_LAUNCH_TIME, enabled).apply()
    }

    fun getNextNotificationTime(): Long {
        return sharedPreferences.getLong(LAST_LAUNCH_TIME, 0)
    }

    fun saveBotId(botId: String) {
        sharedPreferences.edit().putString(BOT_ID, botId).apply()
    }

    fun saveChatId(chatId: String) {
        sharedPreferences.edit().putString(CHAT_ID, chatId).apply()
    }

    fun getBotId() = sharedPreferences.getString(BOT_ID, "unavailable")

    fun getChatId() = sharedPreferences.getString(CHAT_ID, "unavailable")

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }
}