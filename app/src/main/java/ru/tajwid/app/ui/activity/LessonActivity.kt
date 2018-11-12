package ru.tajwid.app.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_lesson.*
import ru.tajwid.app.R
import ru.tajwid.app.content.data.LessonSection
import ru.tajwid.app.content.db.DbManager
import ru.tajwid.app.ui.view.*
import ru.tajwid.app.utils.FontUtils
import ru.tajwid.app.utils.HighlightUtils
import ru.tajwid.app.utils.ViewStateHelper


private const val EXTRA_MODULE_ID = "module_id"
private const val EXTRA_LESSON_ID = "lesson_id"
private const val plainText = "plainText"
private const val arabic = "arabic"
private const val highlightedText = "highlitedText"
private const val EXERCISE_REQUEST_CODE = 101

class LessonActivity : BaseActivity(), PlayerView.OnStateChangedListener, ViewStateHelper.ViewStateCallback<ImageView> {

    private var lessonId = 0
    private var moduleId = 0
    private var shouldShowSmallPlayer = false
    private val progressDao = DbManager.get().getProgressDAO()
    private val handler = Handler()
    private val viewStateHelper = ViewStateHelper(this)

    companion object {
        fun getIntent(context: Context, moduleId: Int, lesson_id: Int): Intent {
            val intent = Intent(context, LessonActivity::class.java)
            intent.putExtra(EXTRA_MODULE_ID, moduleId)
            intent.putExtra(EXTRA_LESSON_ID, lesson_id)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        FontUtils.setTextViewFont(lesson_text_go_to_next, R.font.proxima_nova_semibold)

        moduleId = intent.getIntExtra(EXTRA_MODULE_ID, 0)
        lessonId = intent.getIntExtra(EXTRA_LESSON_ID, 0)

        setupSimpleToolbar(lessons_toolbar, getString(R.string.lesson_title, lessonId + 1))
        lessons_toolbar.inflateMenu(R.menu.lesson)
        lessons_toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
            if (it.itemId == R.id.action_lesson_font_settings) {
                lesson_font_settings.visibility = if (lesson_font_settings.visibility == VISIBLE) GONE else VISIBLE
                return@OnMenuItemClickListener true
            }
            return@OnMenuItemClickListener false
        })

        val allCardsStates = progressDao.getAllCardsStates(moduleId, lessonId)

        val lesson = DbManager.get().getModulesDAO().get(moduleId)!!.lessons[lessonId]
        if (lesson!!.exercises.isEmpty()) {
            lesson_go_to_exercises_container.visibility = GONE
        } else {
            lesson_go_to_exercises_container.setOnClickListener { onExercisesClick() }
        }
        lesson_player.setup(moduleId, lessonId, lesson.sections.sumBy { it.cards.size }, this)

        lesson_scroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            run {
                if (scrollY > oldScrollY) {
                    if (!shouldShowSmallPlayer) {
                        shouldShowSmallPlayer = true
                        handler.removeCallbacksAndMessages(null)
                        handler.postDelayed({ lesson_player.setSmallView(true) }, 200)
                    }
                } else if (scrollY < 20) {
                    shouldShowSmallPlayer = false
                    handler.removeCallbacksAndMessages(null)
                    lesson_player.setSmallView(false)
                }
            }
        })

        //val sounds = resources.assets.list("audio")
        var soundId = 0

        for (sectionId in 0 until lesson.sections.size) {
            val section = lesson.sections[sectionId] as LessonSection

            val headerView = LessonSectionHeaderView(this)
            headerView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            headerView.setLetter(section.arabicTitle!!)
            headerView.setTitle(section.title)
            lesson_content_container.addView(headerView)

            for (cardId in 0 until section.cards.size) {
                val card = section.cards[cardId]
                if (!TextUtils.isEmpty(card?.title)) {
                    val view = LessonCardTitleTextView(this)
                    view.gravity = Gravity.CENTER
                    view.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    view.text = card?.title
                    lesson_content_container.addView(view)
                }
                val playerIconView = ImageView(this)
                lesson_content_container.addView(playerIconView)
                playerIconView.setImageResource(R.drawable.ic_lesson_player)
                val playerIconLayoutParams = playerIconView.layoutParams as LinearLayout.LayoutParams
                playerIconLayoutParams.width = WRAP_CONTENT
                playerIconLayoutParams.height = WRAP_CONTENT
                playerIconLayoutParams.gravity = Gravity.CENTER_HORIZONTAL
                (playerIconLayoutParams as ViewGroup.MarginLayoutParams).topMargin = resources.getDimension(R.dimen.dimen_8dp).toInt()
                playerIconView.layoutParams = playerIconLayoutParams
                viewStateHelper.add(playerIconView)
                val fileName = "audio_${moduleId + 1}_${lessonId + 1}_${soundId + 1}"
                val isSound = resources
                        .getIdentifier(fileName, "raw", applicationContext.packageName) != 0
                val clickable = isSound/*sounds.contains(fileName)*/ && getSoundFileSize(fileName) >= 10240L
                if (!clickable) {
                    playerIconView.visibility = GONE
                } else {
                    setCardPlayerClickListener(playerIconView, soundId)
                }
                for (contentItemId in 0 until card!!.contentItems.size) {
                    var view: View? = null
                    when {
                        card.contentItems[contentItemId]!!.type == plainText -> {
                            view = LessonPlainTextView(this)
                            view.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                            HighlightUtils.setHighlightedText(view, card.contentItems[contentItemId]!!.content)
                            lesson_content_container.addView(view)
                        }
                        card.contentItems[contentItemId]!!.type == arabic -> {
                            view = LessonArabicTextView(this)
                            view.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                            HighlightUtils.setHighlightedText(view, card.contentItems[contentItemId]!!.content)
                            if (lessonId == 19 && sectionId == 0 && cardId == 0) {
                                if (contentItemId == 1 || contentItemId == 3 || contentItemId == 5 || contentItemId == 7 || contentItemId == 9 || contentItemId == 11) {
                                    view.textSize = resources.getDimension(R.dimen.text_size_lesson20)
                                }
                            }
                            lesson_content_container.addView(view)
                        }
                        card.contentItems[contentItemId]!!.type == highlightedText -> {
                            view = LessonHighlitedTextView(this)
                            view.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                            HighlightUtils.setHighlightedText(view, card.contentItems[contentItemId]!!.content)
                            lesson_content_container.addView(view)
                        }
                    }
                    if (clickable) {
                        setCardPlayerClickListener(view, soundId)
                    }
                }

                val cardEndView = CircleCheckView(this)
                lesson_content_container.addView(cardEndView)
                val lessonViewLayoutParams = cardEndView.layoutParams as ViewGroup.MarginLayoutParams?
                lessonViewLayoutParams!!.topMargin = resources.getDimension(R.dimen.dimen_20dp).toInt()
                lessonViewLayoutParams.bottomMargin = resources.getDimension(R.dimen.dimen_16dp).toInt()
                cardEndView.setOnClickListener {
                    cardEndView.setDone(progressDao.toggleCardState(moduleId, lessonId, sectionId, cardId))
                    updateProgress()
                }
                cardEndView.setDone(allCardsStates[sectionId]!![cardId]!!)

                val shadowView = View(this)
                lesson_content_container.addView(shadowView)
                val shadowViewLayoutParams = shadowView.layoutParams
                shadowViewLayoutParams.width = MATCH_PARENT
                shadowViewLayoutParams.height = resources.getDimension(R.dimen.dimen_4dp).toInt()
                shadowView.layoutParams = shadowViewLayoutParams
                shadowView.background = ContextCompat.getDrawable(this, R.drawable.exercise_shadow_gradient_top)

                val marginView = View(this)
                lesson_content_container.addView(marginView)
                val marginViewLayoutParams = marginView.layoutParams
                marginViewLayoutParams.width = MATCH_PARENT
                marginViewLayoutParams.height = resources.getDimension(R.dimen.dimen_20dp).toInt()
                marginView.layoutParams = marginViewLayoutParams
                marginView.setBackgroundColor(ContextCompat.getColor(this, R.color.white_text))
                soundId++
            }
        }

        val defaultTextSize = resources.getDimension(R.dimen.text_size_20sp)
        lesson_font_settings.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                for (i in 0 until lesson_content_container.childCount) {
                    val view = lesson_content_container.getChildAt(i)
                    if (view is LessonPlainTextView) {
                        if (progress == 0) {
                            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize)
                        } else {
                            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize + defaultTextSize * (progress.toFloat() * 0.03f))
                        }
                    }
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        updateProgress()
    }

    private fun getSoundFileSize(fileName: String): Long {
        return resources
                .openRawResourceFd(
                        resources.getIdentifier(fileName, "raw", applicationContext.packageName)
                ).length
    }

    private fun setCardPlayerClickListener(view: View?, soundId: Int) {
        view?.setOnClickListener { lesson_player.playSection(soundId) }
    }

    private fun updateProgress() {
        lesson_progress.progress = 1 * 100 / progressDao.getLessonScreensCount(moduleId, lessonId)
    }

    private fun onExercisesClick() {
        startActivityForResult(ExerciseActivity.getIntent(this, moduleId, lessonId), EXERCISE_REQUEST_CODE)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == EXERCISE_REQUEST_CODE &&
                resultCode == RESULT_OK &&
                data != null && data.hasExtra(ExerciseActivity.EXTRA_FINISH_LESSONS)) {
            finish()
        }
    }

    override fun onPlayingStart(section: Int) {
        viewStateHelper.setEnabled(section)
    }

    override fun onPlayingStop(section: Int) {
        viewStateHelper.setEnabled(-1)
    }

    override fun handleViewState(view: ImageView, isEnabled: Boolean) {
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_lesson_player)?.mutate()
        if (isEnabled) {
            drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
        } else {
            drawable?.clearColorFilter()
        }
        view.setImageDrawable(drawable)
    }

    override fun onPause() {
        lesson_player.destroy()
        super.onPause()
    }
}

