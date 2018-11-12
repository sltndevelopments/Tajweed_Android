package ru.tajwid.app.content.db.dao

import ru.tajwid.app.content.data.Module

/**
 * Created by BArtWell on 17.02.2018.
 */

class ModulesDAO : BaseDAO<Module>() {

    override fun getModelClass(): Class<Module> = Module::class.java
}