package ru.tajwid.app.ui.fragment

import androidx.fragment.app.Fragment
import ru.tajwid.app.ui.activity.ExerciseActivity

abstract class ExerciseFragment : Fragment() {
    var isLastExercise = false

    companion object {
        const val EXTRA_EXERCISE = "exercise"
        const val EXTRA_IS_LAST = "is_last"
        const val CAN_GO_NEXT_ANIM_DELAY = 1000L
    }

    fun onGoNextClick() {
        if (isLastExercise) {
            (activity as ExerciseActivity).goToLessonsListActivity()
        } else {
            (activity as ExerciseActivity).goToNextFragment()
        }
    }
}