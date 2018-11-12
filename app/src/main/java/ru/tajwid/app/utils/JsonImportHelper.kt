package ru.tajwid.app.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.tajwid.app.R
import ru.tajwid.app.content.data.Book
import ru.tajwid.app.content.data.Progress
import ru.tajwid.app.content.db.DbManager
import java.io.IOException

/**
 * Created by BArtWell on 17.02.2018.
 */

private const val TAG = "JsonImportHelper"

class JsonImportHelper {
    companion object {

        private fun loadJsonFromRaw(context: Context) = try {
            val inputStream = context.resources.openRawResource(R.raw.book)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }

        fun import(context: Context) {
            val modulesDAO = DbManager.get().getModulesDAO()
            val progressDAO = DbManager.get().getProgressDAO()
            if (modulesDAO.count() == 0L) {
                val modules = Gson().fromJson<Book>(loadJsonFromRaw(context)).modules
                for (module in modules) {
                    modulesDAO.insert(module)
                }
                for (module in modules) {
                    for (lessonId in 0 until module.lessons.size) {
                        val lesson = module.lessons[lessonId]!!
                        for (sectionId in 0 until lesson.sections.size) {
                            val section = lesson.sections[sectionId]!!
                            for (cardId in 0 until section.cards.size) {
                                val progress = Progress()
                                progress.module = module.id
                                progress.lesson = lessonId
                                progress.section = sectionId
                                progress.card = cardId
                                progressDAO.insert(progress)
                            }
                        }
                        for (exerciseId in 0 until lesson.exercises.size) {
                            val progress = Progress()
                            progress.module = module.id
                            progress.lesson = lessonId
                            progress.exercise = exerciseId
                            progressDAO.insert(progress)
                        }
                    }
                }
            }
            Log.d(TAG, "Imported: ${modulesDAO.count()} modules, ${progressDAO.count()} progress")
        }

        private inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)
    }
}
