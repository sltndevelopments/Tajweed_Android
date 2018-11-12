package ru.tajwid.app.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_exercise_writingbyexample.*
import ru.tajwid.app.R
import ru.tajwid.app.content.data.Exercise
import ru.tajwid.app.ui.activity.ExerciseActivity
import ru.tajwid.app.utils.FontUtils

private const val EXTRA_EXERCISE = "exercise"
private const val EXTRA_IS_LAST = "is_last"

class ExerciseWritingByExampleFragment : Fragment() {

    private var isLastExercise = false

    companion object {
        fun newInstance(exercise: Exercise, isLastExercise: Boolean): ExerciseWritingByExampleFragment {
            val fragment = ExerciseWritingByExampleFragment()
            val arguments = Bundle()
            arguments.putParcelable(EXTRA_EXERCISE, exercise)
            arguments.putBoolean(EXTRA_IS_LAST, isLastExercise)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exercise_writingbyexample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exercise = arguments!!.getParcelable<Exercise>(EXTRA_EXERCISE)
        isLastExercise = arguments?.getBoolean(EXTRA_IS_LAST) ?: false

        exercise_writing_example_image.setOnClickListener { onExerciseTestClick() }

        exercise_writing_example_title.text = exercise?.content?.title
        FontUtils.setTextViewFont(exercise_writing_example_title, R.font.montserrat_regular)

        exercise_writing_example.text = exercise?.content?.example
//        FontUtils.setTextViewFont(exercise_writing_example, FontUtils.ARABIC_FONT)
//        FontUtils.setTextViewFont(exercise_writing_example_text, FontUtils.ARABIC_FONT)

        if (isLastExercise) {
            exercise_writing_example_image.setImageResource(R.drawable.ic_go_to_lesson)
            exercise_writing_example_text.setText(R.string.finishing)
        }
    }

    private fun onExerciseTestClick() {
        if (isLastExercise) {
            (activity as ExerciseActivity).goToLessonsListActivity()
        } else {
            (activity as ExerciseActivity).goToNextFragment()
        }
    }
}
