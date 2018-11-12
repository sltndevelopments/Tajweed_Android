package ru.tajwid.app.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import kotlinx.android.synthetic.main.activity_exercise.*
import ru.tajwid.app.R
import ru.tajwid.app.content.data.Lesson
import ru.tajwid.app.content.db.DbManager
import ru.tajwid.app.ui.fragment.*


private const val EXTRA_MODULE_ID = "module_id"
private const val EXTRA_LESSON_ID = "lesson_id"

class ExerciseActivity : BaseActivity() {

    private var moduleId = 0
    private var lessonId = 0
    private var exerciseId = 0
    private var lesson: Lesson? = null
    private val progressDao = DbManager.get().getProgressDAO()
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    //private lateinit var sounds: List<String>

    companion object {

        const val EXTRA_FINISH_LESSONS = "finish_lessons"

        fun getIntent(context: Context, moduleId: Int, lessonId: Int): Intent {
            val intent = Intent(context, ExerciseActivity::class.java)
            intent.putExtra(EXTRA_MODULE_ID, moduleId)
            intent.putExtra(EXTRA_LESSON_ID, lessonId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        moduleId = intent.getIntExtra(EXTRA_MODULE_ID, 0)
        lessonId = intent.getIntExtra(EXTRA_LESSON_ID, 0)

        //sounds = resources.assets.list("audio").toList()

        lesson = DbManager.get().getModulesDAO().get(moduleId)?.lessons?.get(lessonId)
        setupSimpleToolbar(exercise_toolbar, "")
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setOnPreparedListener { mediaPlayer.start() }
        mediaPlayer.setOnCompletionListener { shutdownPlayer() }
        goToNextFragment()
    }

    fun goToNextFragment() {
        if (exerciseId > 0) {
            setExerciseCompleted()
        }
        exercise_toolbar.title = getString(R.string.exercise_title, lessonId + 1, exerciseId + 1)
        val exercise = lesson?.exercises?.get(exerciseId)
        if (exercise != null) {
            val isLastExercise = exerciseId == lesson?.exercises?.size?.minus(1)
            when (exercise.type) {
                "test" -> replaceFragment(ExerciseTestFragment.newInstance(exercise, isLastExercise))
                "pronounce" -> replaceFragment(ExercisePronounceFragment.newInstance(exercise, isLastExercise))
                "writingByExample" -> replaceFragment(ExerciseWritingByExampleFragment.newInstance(exercise, isLastExercise))
                "writingByTranscription" -> replaceFragment(ExerciseWritingByTranscriptionFragment.newInstance(exercise, isLastExercise))
                "reading" -> replaceFragment(ExerciseReadingFragment.newInstance(exercise, isLastExercise))
            }
            /*if (exercise.type == "test") {
                replaceFragment(ExerciseTestFragment.newInstance(exercise, isLastExercise))
            } else if (exercise.type == "pronounce") {
                replaceFragment(ExercisePronounceFragment.newInstance(exercise, isLastExercise))
            } else if (exercise.type == "writingByExample") {
                replaceFragment(ExerciseWritingByExampleFragment.newInstance(exercise, isLastExercise))
            } else if (exercise.type == "writingByTranscription") {
                replaceFragment(ExerciseWritingByTranscriptionFragment.newInstance(exercise, isLastExercise))
            } else if (exercise.type == "reading") {
                replaceFragment(ExerciseReadingFragment.newInstance(exercise, isLastExercise))
            }*/
            exercise_scroll_view.fullScroll(View.FOCUS_UP)
        }
        updateProgress()
        exerciseId++
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.exercise_fragment_container, fragment, fragment.toString())
        ft.commit()
    }

    fun goToLessonsListActivity() {
        setExerciseCompleted()
        val intent = Intent()
        intent.putExtra(ExerciseActivity.EXTRA_FINISH_LESSONS, true)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun playSound(wordId: Int) {
        var cardsCount = 0
        for (section in lesson?.sections!!) {
            cardsCount += section.cards.size
        }
        val cardId = exerciseId + cardsCount
        playSound(getAudioName(cardId, wordId))
    }

    fun isWordHasSound(wordId: Int): Boolean {
        var cardsCount = 0
        for (section in lesson?.sections!!) {
            cardsCount += section.cards.size
        }
        val cardId = exerciseId + cardsCount
        return resources.getIdentifier(
                getAudioName(cardId, wordId),
                "raw",
                applicationContext.packageName
        ) != 0
        //sounds.contains((moduleId + 1).toString() + "_" + (lessonId + 1) + "_" + (cardId) + "_" + (wordId + 1) + ".mp3")
    }

    fun getAudioName(cardId: Int, wordId: Int): String {
        return "audio_" + (moduleId + 1) + "_" + (lessonId + 1) + "_" + (cardId) + "_" + (wordId + 1)
    }

    private fun setExerciseCompleted() {
        DbManager.get().getProgressDAO().setExerciseCompleted(moduleId, lessonId, exerciseId - 1)
    }

    private fun updateProgress() {
        exercise_progress.progress = (exerciseId + 2) * 100 / progressDao.getLessonScreensCount(moduleId, lessonId)
    }

    private fun playSound(sound: String) {
        try {
            if (mediaPlayer.isPlaying) {
                shutdownPlayer()
            }
            val descriptor = resources.openRawResourceFd(resources.getIdentifier(sound, "raw", applicationContext.packageName))
            mediaPlayer.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
            mediaPlayer.prepareAsync()
            descriptor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shutdownPlayer() {
        mediaPlayer.stop()
        mediaPlayer.reset()
    }

    override fun onPause() {
        shutdownPlayer()
        super.onPause()
    }
}
