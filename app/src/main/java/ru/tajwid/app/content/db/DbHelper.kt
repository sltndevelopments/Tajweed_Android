package ru.tajwid.app.content.db

import android.content.Context
import ru.tajwid.app.content.db.dao.ModulesDAO
import ru.tajwid.app.content.db.dao.ProgressDAO

/**
 * Created by BArtWell on 17.02.2018.
 */
class DbHelper internal constructor(context: Context) {

    private var modulesDao: ModulesDAO? = null
    private var progressDao: ProgressDAO? = null

    init {
        Realm.init(context)
        val realmConfiguration = RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(DATABASE_VERSION)
                .migration { realm, _, _ ->
                    realm.delete("Module")
                    realm.delete("Lesson")
                    realm.delete("LessonSection")
                    realm.delete("LessonCard")
                    realm.delete("LessonCardContentItem")
                    realm.delete("Exercise")
                    realm.delete("ExerciseContent")
                }
                //.deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    fun getModulesDAO(): ModulesDAO {
        if (modulesDao == null) {
            modulesDao = ModulesDAO()
        }
        return modulesDao as ModulesDAO
    }

    fun getProgressDAO(): ProgressDAO {
        if (progressDao == null) {
            progressDao = ProgressDAO()
        }
        return progressDao as ProgressDAO
    }

    internal fun destroy() {
        Realm.getDefaultInstance()
    }

    companion object {
        /*каждый раз, когда меняется book.json, нужно увеличивать версию*/
        private const val DATABASE_VERSION = 18L

        fun deleteAll() {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            realm.deleteAll()
            realm.commitTransaction()
        }
    }
}
