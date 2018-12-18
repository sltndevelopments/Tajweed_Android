package ru.tajwid.app.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_exercise_pronounce.*
import ru.tajwid.app.R
import ru.tajwid.app.content.data.Exercise
import ru.tajwid.app.ui.activity.ExerciseActivity
import ru.tajwid.app.ui.view.ExercisePronounceTextView
import ru.tajwid.app.utils.FontUtils
import ru.tajwid.app.utils.highlight.ArabicHighlighter


private const val EXTRA_EXERCISE = "exercise"
private const val EXTRA_IS_LAST = "is_last"
private const val WORDS_DELIMITER = " ، "

class ExercisePronounceFragment : Fragment() {

    private var isLastExercise = false

    companion object {
        fun newInstance(exercise: Exercise, isLastExercise: Boolean): ExercisePronounceFragment {
            val fragment = ExercisePronounceFragment()
            val arguments = Bundle()
            arguments.putParcelable(EXTRA_EXERCISE, exercise)
            arguments.putBoolean(EXTRA_IS_LAST, isLastExercise)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exercise_pronounce, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exercise = arguments!!.getParcelable<Exercise>(EXTRA_EXERCISE)
        isLastExercise = arguments?.getBoolean(EXTRA_IS_LAST) ?: false

        exercise_pronounce_image.setOnClickListener { onExercisePronounceClick() }

        exercise_pronounce_title.text = exercise.content?.let {
            ArabicHighlighter(it.title).getHighlighted(
                    ResourcesCompat.getFont(view.context, FontUtils.getArabicTypefaceResId())
            )
        }

        FontUtils.setTextViewFont(exercise_pronounce_title, FontUtils.getRegularTypefaceResId())
        FontUtils.setTextViewFont(exercise_pronounce_text_finishing, R.font.proxima_nova_semibold)

        var isWordsSoundable = false
        var wordId = 0
        for (row in exercise?.content?.rows!!) {
            val rowView = ExercisePronounceTextView(view.context)
            exercise_pronounce_content_container.addView(rowView)
            val layoutParams = rowView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.topMargin = resources.getDimension(R.dimen.dimen_20dp).toInt()
            rowView.layoutParams = layoutParams

            val spannableString = SpannableString(row)
            val words = row.split(WORDS_DELIMITER)
            var wordStartPosition = 0
            for (word in words) {
                val wordEndPosition = wordStartPosition + word.length
                spannableString.setSpan(createClickableSpan(wordId), wordStartPosition, wordEndPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                wordStartPosition = wordEndPosition + WORDS_DELIMITER.length
                if (!isWordsSoundable && (activity as ExerciseActivity).isWordHasSound(wordId)) {
                    isWordsSoundable = true
                }
                wordId++
            }

            rowView.text = spannableString
            rowView.movementMethod = LinkMovementMethod.getInstance()
            rowView.highlightColor = Color.TRANSPARENT
        }

        exercise_pronounce_sound.visibility = if (isWordsSoundable) VISIBLE else GONE

        if (isLastExercise) {
            exercise_pronounce_image.setImageResource(R.drawable.ic_go_to_lesson)
            exercise_pronounce_text_finishing.setText(R.string.finishing)

        }
    }

    private fun createClickableSpan(wordId: Int): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(textView: View) {
                (activity as ExerciseActivity).playSound(wordId)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
    }

    private fun onExercisePronounceClick() {
        if (isLastExercise) {
            (activity as ExerciseActivity).goToLessonsListActivity()
        } else {
            (activity as ExerciseActivity).goToNextFragment()
        }
    }
}
