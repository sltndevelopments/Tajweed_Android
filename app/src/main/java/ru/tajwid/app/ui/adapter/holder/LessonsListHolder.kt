package ru.tajwid.app.ui.adapter.holder

import android.support.v7.widget.RecyclerView
import ru.tajwid.app.R
import ru.tajwid.app.content.data.Lesson
import ru.tajwid.app.ui.adapter.LessonsListAdapter
import ru.tajwid.app.ui.view.LessonsListItemView

/**
 * Created by BArtWell on 17.02.2018.
 */
class LessonsListHolder(private val parent: LessonsListItemView) : RecyclerView.ViewHolder(parent) {
    fun bind(lesson: Lesson, isFilled: Boolean, listener: LessonsListAdapter.OnClickListener?) {
        parent.setNumber(adapterPosition + 1, isFilled)
        parent.setTitle(lesson.title)

        var cardsCount = 0
        for (section in lesson.sections) {
            cardsCount += section.cards.size
        }

        val context = parent.context
        val sectionsInfo = context.resources.getQuantityString(R.plurals.lessons_list_sections_info, cardsCount, cardsCount)
        val exercisesInfo: String
        if (lesson.exercises.size == 0) {
            exercisesInfo = context.resources.getString(R.string.lessons_list_exercise_info)
        } else {
            exercisesInfo = context.resources.getQuantityString(R.plurals.lessons_list_exercises_info, lesson.exercises.size, lesson.exercises.size)
        }
        parent.setInfo(context.getString(R.string.lessons_list_info_format, sectionsInfo, exercisesInfo))
        parent.setOnClickListener { listener?.onClick(adapterPosition) }
    }
}