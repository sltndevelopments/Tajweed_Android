package ru.tajwid.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.fragment_exercise_writingbytranscription.*
import ru.tajwid.app.R
import ru.tajwid.app.content.data.Exercise
import ru.tajwid.app.utils.FontUtils
import ru.tajwid.app.utils.highlight.ArabicHighlighter

class ExerciseWritingByTranscriptionFragment : ExerciseFragment() {

    private lateinit var exercise: Exercise
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
        exercise = requireArguments().getParcelable(EXTRA_EXERCISE)!!
        isLastExercise = arguments?.getBoolean(EXTRA_IS_LAST) ?: false

        exercise_writing_by_transcription_go_next.setOnClickListener { onExerciseTestClick() }

        exercise_writing_title.text = exercise.content?.title
        FontUtils.setTextViewFont(exercise_writing_title, FontUtils.getRegularTypefaceResId())

        exercise_writing_transcription.text = exercise.content?.transcription?.let {
            ArabicHighlighter(it).getHighlighted(
                ResourcesCompat.getFont(view.context, FontUtils.getArabicTypefaceResId())
            )
        } ?: run { null }
        FontUtils.setTextViewFont(exercise_writing_transcription, FontUtils.getRegularTypefaceResId())

        FontUtils.setTextViewFont(exercise_writing_text, FontUtils.getRegularTypefaceResId())
    }

    private fun onExerciseTestClick() {
        if (isCorrectWritingShown) {
            onGoNextClick()
        } else {
            isCorrectWritingShown = true
            exercise_writing_correct.text = exercise.content?.correctWriting?.let {
                ArabicHighlighter(it).getHighlighted(
                    ResourcesCompat.getFont(exercise_writing_correct.context, FontUtils.getArabicTypefaceResId())
                )
            } ?: run { null }
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
