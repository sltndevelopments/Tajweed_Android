package ru.tajwid.app.content.db.dao

import ru.tajwid.app.content.data.Progress

private const val FIELD_MODULE = "module"
private const val FIELD_LESSON = "lesson"
private const val FIELD_SECTION = "section"
private const val FIELD_CARD = "card"
private const val FIELD_EXERCISE = "exercise"
private const val FIELD_IS_COMPLETED = "isCompleted"

open class ProgressDAO : BaseDAO<Progress>() {

    override fun getModelClass(): Class<Progress> = Progress::class.java

    fun getTotalProgressInfo(): Pair<Int, Int> {
        val progresses = getRealm().where(getModelClass()).findAll().sort(FIELD_IS_COMPLETED)

        val total = progresses
                .distinctBy { progress -> "${progress.module}:${progress.lesson}" }
                .size

        val completed = progresses
                .distinctBy { progress -> "${progress.module}:${progress.lesson}" }
                .filter { progress -> progress.isCompleted }
                .size

        return Pair(total, completed)
    }

    fun getModulesProgressInfo(): Pair<HashMap<Int, Int>, HashMap<Int, Int>> {
        val progresses = getRealm().where(getModelClass()).findAll().sort(FIELD_IS_COMPLETED)
        val grouped = progresses.distinctBy { "${it.module}:${it.lesson}" }
        val total = HashMap<Int, Int>()
        val completed = HashMap<Int, Int>()
        for (progress in grouped) {
            total[progress.module] = (total[progress.module] ?: 0) + 1
            if (completed[progress.module] == null) {
                completed[progress.module] = 0
            }
            if (progress.isCompleted) {
                completed[progress.module] = completed[progress.module]!!.plus(1)
            }
        }

        return Pair(total, completed)
    }

    fun toggleCardState(module: Int, lesson: Int, section: Int, card: Int): Boolean {
        val realm = getRealm()
        val progress = realm.where(getModelClass())
                .equalTo(FIELD_MODULE, module)
                .equalTo(FIELD_LESSON, lesson)
                .equalTo(FIELD_SECTION, section)
                .equalTo(FIELD_CARD, card)
                .findFirst()
        if (progress != null) {
            realm.beginTransaction()
            progress.isCompleted = !progress.isCompleted
            realm.commitTransaction()
            return progress.isCompleted
        }
        return false
    }

    fun getAllLessonsStates(module: Int, lesson: Int): HashMap<Int, Boolean> {
        val result = HashMap<Int, Boolean>()
        val progresses = getRealm().where(getModelClass())
                .equalTo(FIELD_MODULE, module)
                .findAll()
        for (progress in progresses) {
            if (result.containsKey(progress.lesson)) {
                result[progress.lesson] = result[progress.lesson]!! && progress.isCompleted
            } else {
                result[progress.lesson] = progress.isCompleted
            }
        }
        return result
    }

    fun getAllCardsStates(module: Int, lesson: Int): HashMap<Int, HashMap<Int, Boolean>> {
        val result = HashMap<Int, HashMap<Int, Boolean>>()
        val progresses = getRealm().where(getModelClass())
                .equalTo(FIELD_MODULE, module)
                .equalTo(FIELD_LESSON, lesson)
                .findAll()
        for (progress in progresses) {
            if (result[progress.section] == null) {
                result[progress.section] = HashMap()
            }
            result[progress.section]!![progress.card] = progress.isCompleted
        }
        return result
    }

    fun setExerciseCompleted(module: Int, lesson: Int, exercise: Int) {
        val realm = getRealm()
        val progress = realm.where(getModelClass())
                .equalTo(FIELD_MODULE, module)
                .equalTo(FIELD_LESSON, lesson)
                .equalTo(FIELD_EXERCISE, exercise)
                .findFirst()
        if (progress != null) {
            realm.beginTransaction()
            progress.isCompleted = true
            realm.commitTransaction()
        }
    }

    fun getLessonScreensCount(module: Int, lesson: Int): Int {
        return getRealm().where(getModelClass())
                .equalTo(FIELD_MODULE, module)
                .equalTo(FIELD_LESSON, lesson)
                .notEqualTo(FIELD_EXERCISE, Progress.NO_VALUE)
                .count()
                .plus(1) // Количество экранов упражнений + экран урока
                .toInt()
    }
}