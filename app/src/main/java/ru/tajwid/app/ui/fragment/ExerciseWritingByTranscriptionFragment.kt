package ru.tajwid.app.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_exercise_writingbytranscription.*
import ru.tajwid.app.R
import ru.tajwid.app.content.data.Exercise
import ru.tajwid.app.ui.activity.ExerciseActivity
import ru.tajwid.app.utils.FontUtils

private const val EXTRA_EXERCISE = "exercise"
private const val EXTRA_IS_LAST = "is_last"

class ExerciseWritingByTranscriptionFragment : Fragment() {

    private lateinit var exercise: Exercise
    private var isLastExercise = false
    private var isCorrectWritingShown = false

    companion object {
        fun newInstance(exercise: Exercise, isLastExercise: Boolean): ExerciseWritingByTranscriptionFragment {
            val fragment = ExerciseWritingByTranscriptionFragment()
            val arguments = Bundle()
            arguments.putParcelable(EXTRA_EXERCISE, exercise)
            arguments.putBoolean(EXTRA_IS_LAST, isLastExercise)
            fragment.arguments = arguments
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exercise_writingbytranscription, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exercise = arguments!!.getParcelable(EXTRA_EXERCISE)
        isLastExercise = arguments?.getBoolean(EXTRA_IS_LAST) ?: false

        exercise_writing_by_transcription_go_next.setOnClickListener { onExerciseTestClick() }

        exercise_writing_title.text = exercise.content?.title
        FontUtils.setTextViewFont(exercise_writing_title, R.font.montserrat_regular)

        exercise_writing_transcription.text = exercise.content?.transcription
        FontUtils.setTextViewFont(exercise_writing_transcription, R.font.montserrat_regular)

        FontUtils.setTextViewFont(exercise_writing_text, R.font.montserrat_regular)
    }

    private fun onExerciseTestClick() {
        if (isCorrectWritingShown) {
            if (isLastExercise) {
                (activity as ExerciseActivity).goToLessonsListActivity()
            } else {
                (activity as ExerciseActivity).goToNextFragment()
            }
        } else {
            isCorrectWritingShown = true
            exercise_writing_correct.text = exercise.content?.correctWriting
//            FontUtils.setTextViewFont(exercise_writing_correct, FontUtils.ARABIC_FONT)

            if (isLastExercise) {
                exercise_writing_image.setImageResource(R.drawable.ic_go_to_lesson)
                exercise_writing_text.setText(R.string.finishing)
                FontUtils.setTextViewFont(exercise_writing_text, R.font.proxima_nova_semibold)

            } else {
                exercise_writing_image.setImageResource(R.drawable.ic_go_to_lesson)
                exercise_writing_text.setText(R.string.onward)
            }
        }
    }
}
