package ru.tajwid.app.content.db.dao

import io.realm.Realm
import io.realm.RealmObject
import ru.tajwid.app.content.data.AutoIncrementIdentify

/**
 * Created by BArtWell on 17.02.2018.
 */
abstract class BaseDAO<T : RealmObject> internal constructor() {

    private val mPrimaryKeyColumnName: String
    internal abstract fun getModelClass(): Class<T>

    internal fun getRealm() = Realm.getDefaultInstance()

    fun getAll(): List<T>? {
        try {
            val results = getRealm().where(getModelClass()).findAll()
            return results.subList(0, results.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun clear() {
        val realm = getRealm()
        realm.beginTransaction()
        realm.delete(getModelClass())
        realm.commitTransaction()
    }

    fun get(id: Int): T? {
        try {
            return getRealm().where(getModelClass()).equalTo(mPrimaryKeyColumnName, id).findFirst()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun count() = getRealm().where(getModelClass()).count()

    open fun insert(obj: T?) {
        if (obj != null) {
            if (obj is AutoIncrementIdentify) {
                obj.setAutoIncrementId(getNextId())
            }
            val realm = getRealm()
            realm.beginTransaction()
            realm.copyToRealm(obj)
            realm.commitTransaction()
        }
    }

    fun update(obj: T?) {
        if (obj != null) {
            val realm = getRealm()
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(obj)
            realm.commitTransaction()
        }
    }

    fun delete(obj: T?) {
        obj?.deleteFromRealm()
    }

    fun delete(id: Int) {
        delete(get(id))
    }

    init {
        mPrimaryKeyColumnName = getRealm().schema.get(getModelClass().simpleName)!!.primaryKey
    }

    private fun getNextId(): Int {
        try {
            val number = getRealm().where(getModelClass()).max("id")
            return if (number != null) {
                number.toInt() + 1
            } else {
                0
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            return 0
        }

    }
}
