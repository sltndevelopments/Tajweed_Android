package ru.tajwid.app.content.db

import android.content.Context

/**
 * Created by BArtWell on 17.02.2018.
 */
class DbManager {

    companion object {
        private var dbHelper: DbHelper? = null

        fun get(): DbHelper {
            if (dbHelper == null) {
                throw IllegalStateException("DatabaseHelper is not exist")
            }
            return dbHelper as DbHelper
        }

        fun init(context: Context) {
            dbHelper = DbHelper(context)
        }

        fun destroy() {
            if (dbHelper != null) {
                dbHelper!!.destroy()
                dbHelper = null
            }
        }
    }
}