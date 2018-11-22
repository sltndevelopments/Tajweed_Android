package ru.tajwid.app.ui.fragment

import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_exercise_reading.*
import ru.tajwid.app.R
import ru.tajwid.app.content.data.Exercise
import ru.tajwid.app.ui.activity.ExerciseActivity
import ru.tajwid.app.ui.view.ExerciseReadingTextView
import ru.tajwid.app.utils.FontUtils
import ru.tajwid.app.utils.highlight.ArabicHighlighter


private const val EXTRA_EXERCISE = "exercise"
private const val EXTRA_IS_LAST = "is_last"

class ExerciseReadingFragment : Fragment() {

    private var isLastExercise = false

    companion object {
        fun newInstance(exercise: Exercise, isLastExercise: Boolean): ExerciseReadingFragment {
            val fragment = ExerciseReadingFragment()
            val arguments = Bundle()
            arguments.putParcelable(EXTRA_EXERCISE, exercise)
            arguments.putBoolean(EXTRA_IS_LAST, isLastExercise)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exercise_reading, container, false)
    }

    private lateinit var isCorrectWordsFound: Array<Boolean>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exercise = arguments!!.getParcelable<Exercise>(EXTRA_EXERCISE)
        isLastExercise = arguments?.getBoolean(EXTRA_IS_LAST) ?: false

        exercise_reading_image.setOnClickListener { onExerciseTestClick() }

        exercise_reading_title.text = exercise.content?.title
        FontUtils.setTextViewFont(exercise_reading_title, R.font.montserrat_regular)

        val paint = ExerciseReadingTextView(context!!).paint
        val maxWidth = getMaxWidth()

        val rows = exercise.content?.text?.split("\n")
        isCorrectWordsFound = exercise.content?.correctWords?.let { it ->
            Array(it.size) { false }
        } ?: run { Array(0) { false } }
        // Расставляем переносы, если строка слишком длинная
        val rowsWithBreaks = mutableListOf<String>()
        for (rowText in rows!!) {
            val words = rowText.split(" ")
            var rowBuffer = ""
            var isFirstIteration = true
            for (word in words) {
                var testString = rowBuffer
                var delimiter = ""
                if (isFirstIteration) {
                    isFirstIteration = false
                } else {
                    testString += " "
                    delimiter = " "
                }
                testString += word
                val rect = Rect()
                paint.getTextBounds(testString, 0, testString.length, rect)
                if (rect.width() >= maxWidth) {
                    rowsWithBreaks.add(delimiter + rowBuffer)
                    rowBuffer = word
                } else {
                    rowBuffer += delimiter + word
                }
            }
            rowsWithBreaks.add(rowBuffer)
        }

        for (rowText in rowsWithBreaks) {
            val rowView = LinearLayout(context)
            rowView.orientation = LinearLayout.HORIZONTAL
            val rowViewLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            rowViewLayoutParams.gravity = Gravity.RIGHT
            rowView.layoutParams = rowViewLayoutParams
            val words = rowText.split(" ")
            for (word in words.reversed()) {
                val rowItemView = LinearLayout(context)
                rowView.addView(rowItemView)
                rowItemView.orientation = LinearLayout.VERTICAL
                val rowItemViewLayoutParams = rowItemView.layoutParams as ViewGroup.MarginLayoutParams
                rowItemViewLayoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
                rowItemViewLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                rowItemViewLayoutParams.topMargin = resources.getDimension(R.dimen.dimen_20dp).toInt()
                rowItemViewLayoutParams.rightMargin = resources.getDimension(R.dimen.dimen_4dp).toInt()
                rowItemView.layoutParams = rowItemViewLayoutParams

                val wordView = ExerciseReadingTextView(context!!)
                rowItemView.addView(wordView)
                val wordViewLayoutParams = wordView.layoutParams as ViewGroup.MarginLayoutParams
                wordViewLayoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
                wordViewLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                wordViewLayoutParams.topMargin = resources.getDimension(R.dimen.dimen_20dp).toInt()
                wordView.layoutParams = wordViewLayoutParams
                wordView.text = ArabicHighlighter(word).getHighlighted(
                        ResourcesCompat.getFont(view.context, FontUtils.getArabicTypefaceResId())
                )

                val underlineView = View(context)
                rowItemView.addView(underlineView)
                val layoutParams = underlineView.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
                layoutParams.height = resources.getDimension(R.dimen.dimen_2dp).toInt()
                layoutParams.topMargin = resources.getDimension(R.dimen.dimen_20dp).toInt()
                underlineView.layoutParams = layoutParams
                underlineView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.default_separator))

                rowItemView.setOnClickListener {
                    val isCorrect = exercise?.content?.correctWords?.let { correctWords ->
                        for (i in 0 until correctWords.size) {
                            if (word == correctWords[i]) {
                                isCorrectWordsFound[i] = true
                                return@let true
                            }
                        }
                        false
                    } ?: run { false }

                    underlineView.setBackgroundColor(ContextCompat.getColor(context!!, if (isCorrect)
                        R.color.shamrock_green
                    else
                        R.color.red))
                    if (!goingNext && isCorrectWordsFound.all { item -> item }) {
                        setCanGoNext()
                    }
                }
            }
            exercise_reading_content_container.addView(rowView)
        }
    }

    private var goingNext = false

    private fun setCanGoNext() {
        goingNext = true
        exercise_reading_go_next.visibility = VISIBLE
        exercise_reading_image.setImageResource(R.drawable.ic_circle_check_green)
        exercise_reading_next.setText(R.string.right)
        Handler().postDelayed({
            try {
                exercise_reading_image.setImageResource(R.drawable.ic_go_to_lesson)
                exercise_reading_next.setText(if (isLastExercise) R.string.finishing else R.string.onward)
            } catch (e: Exception) {
                Log.e(ExerciseReadingFragment::class.java.simpleName, e.localizedMessage)
            }
        }, 1000)
    }

    private fun getMaxWidth(): Int {
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x - resources.getDimension(R.dimen.dimen_8dp).toInt()
    }

    private fun onExerciseTestClick() {
        if (isLastExercise) {
            (activity as ExerciseActivity).goToLessonsListActivity()
        } else {
            (activity as ExerciseActivity).goToNextFragment()
        }
    }
}