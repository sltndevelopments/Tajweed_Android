package ru.tajwid.app.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.fragment_exercise_test.*
import ru.tajwid.app.R
import ru.tajwid.app.content.data.Exercise
import ru.tajwid.app.ui.view.ExerciseVariantsTextView
import ru.tajwid.app.utils.FontUtils
import ru.tajwid.app.utils.highlight.ArabicHighlighter

class ExerciseTestFragment : ExerciseFragment() {

    companion object {
        fun newInstance(exercise: Exercise, isLastExercise: Boolean): ExerciseTestFragment {
            val fragment = ExerciseTestFragment()
            val arguments = Bundle()
            arguments.putParcelable(EXTRA_EXERCISE, exercise)
            arguments.putBoolean(EXTRA_IS_LAST, isLastExercise)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exercise_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exercise = arguments?.getParcelable<Exercise>(EXTRA_EXERCISE)
        isLastExercise = arguments?.getBoolean(EXTRA_IS_LAST) ?: false

        exercise_test_title.text = exercise?.content?.title
        FontUtils.setTextViewFont(exercise_test_title, FontUtils.getRegularTypefaceResId())

        exercise_test_text.text = exercise?.content?.text
        FontUtils.setTextViewFont(exercise_test_text, FontUtils.getRegularTypefaceResId())

        FontUtils.setTextViewFont(exercise_test_text_right, R.font.proxima_nova_semibold)

        exercise_test_image.setImageResource(R.drawable.ic_go_to_lesson)
        exercise_test_text_right.setText(if (isLastExercise) R.string.finishing else R.string.onward)

        exercise_test_go_next.setOnClickListener { onGoNextClick() }

        for (variant in exercise?.content?.variants!!) {
            val variantsView = ExerciseVariantsTextView(requireContext())
            exercise_content_container.addView(variantsView)
            val layoutParams = variantsView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.topMargin = resources.getDimension(R.dimen.dimen_20dp).toInt()
            variantsView.layoutParams = layoutParams
            variantsView.text = ArabicHighlighter(variant).getHighlighted(
                ResourcesCompat.getFont(view.context, FontUtils.getArabicTypefaceResId())
            )
            variantsView.setOnClickListener {
                //                for (i in 0 until exercise_content_container.childCount) {
                //                    val child = exercise_content_container.getChildAt(i) as ExerciseVariantsTextView
                //                    child.isEnabled = false
                //                }
                if (variant == exercise.content?.correctVariant) {
                    variantsView.setTextColor(ContextCompat.getColor(requireContext(), R.color.shamrock_green))
                    variantsView.setDone()
                    setCanGoNext()
                } else {
                    variantsView.setTextColor(ContextCompat.getColor(requireContext(), R.color.test_wrong_answer_color))
                    variantsView.setUndone()
                    //                    loop@ for (i in 0 until exercise_content_container.childCount) {
                    //                        val child = exercise_content_container.getChildAt(i) as ExerciseVariantsTextView
                    //                        if (child.text == exercise.content?.correctVariant) {
                    //                            child.setTextColor(ContextCompat.getColor(this.context!!, R.color.shamrock_green))
                    //                            child.setDone()
                    //                            break@loop
                    //                        }
                    //                    }

                }
            }
        }
    }

    private fun setCanGoNext() {
        exercise_test_go_next.visibility = VISIBLE
        exercise_test_image.setImageResource(R.drawable.ic_circle_check_green)
        exercise_test_text_right.setText(R.string.right)
        Handler().postDelayed({
                                  try {
                                      exercise_test_image.setImageResource(R.drawable.ic_go_to_lesson)
                                      exercise_test_text_right.setText(if (isLastExercise) R.string.finishing else R.string.onward)
                                  } catch (e: Exception) {
                                      e.printStackTrace()
                                  }
                              }, CAN_GO_NEXT_ANIM_DELAY)
    }
}