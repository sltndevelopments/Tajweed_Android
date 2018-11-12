package ru.tajwid.app.ui.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.SeekBar
import kotlinx.android.synthetic.main.view_player.view.*
import ru.tajwid.app.R
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by BArtWell on 14.03.2018.
 */
private const val TAG = "PlayerView"
private const val PROGRESS_UPDATE_DELAY = 500L
private const val SEEK_STEP = 2000
private const val DISABLED_BUTTON_ALPHA = 0.5f
private const val ENABLED_BUTTON_ALPHA = 1f

class PlayerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {

    private var player: MediaPlayer = MediaPlayer()
    private val playerHandler = Handler()
    private var isPlaying = false
    private var isPrepared = false
    private var isSmallViewEnabled = false
    private var isAnimationInProgress = false
    private var moduleId = 0
    private var lessonId = 0
    private var sectionCount = 0
    private var sectionId = 0
    private var filePath = ""
    private lateinit var listener: OnStateChangedListener

    init {
        View.inflate(context, R.layout.view_player, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        player.setAudioStreamType(AudioManager.STREAM_MUSIC)
        player.setOnBufferingUpdateListener(this)
        player.setOnPreparedListener(this)
        player.setOnCompletionListener(this)
        player.setOnErrorListener(this)

        player_previous.setOnClickListener { onPreviousClick() }
        player_backward.setOnClickListener { onBackwardClick() }
        player_play_small.setOnClickListener { onPlayClick() }
        player_play_large.setOnClickListener { onPlayClick() }
        player_forward.setOnClickListener { onForwardClick() }
        player_next.setOnClickListener { onNextClick() }

        player_progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    seekTo(progress)
                }
                player_time_now.text = getTiming(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        player_close.setOnClickListener {
            player.stop()
            isPrepared = false
            isPlaying = false
            listener.onPlayingStop(sectionId)
            setClosed(true)
        }

        setPlayingState(false)
    }

    fun setup(moduleId: Int, lessonId: Int, sectionCount: Int, listener: OnStateChangedListener) {
        this.moduleId = moduleId + 1
        this.lessonId = lessonId + 1
        this.sectionCount = sectionCount
        this.listener = listener
        sectionId = 0
        createFilePath()
        updateNextPrevButtons()
    }

    private fun createFilePath() {
        filePath = "audio_${moduleId}_${lessonId}_${sectionId + 1}"
    }

    private fun onPreviousClick() {
        playAnother(-1)
    }

    private fun onBackwardClick() {
        val newPosition = player.currentPosition - SEEK_STEP
        seekTo(if (newPosition <= 0) 0 else newPosition)
    }

    private fun onPlayClick() {
        setPlayingState(!isPlaying)
        if (isPlaying) {
            play()
        } else {
            internalPause()
        }
    }

    fun setSmallView(shouldUseSmallView: Boolean) {
        if (!isAnimationInProgress && isSmallViewEnabled != shouldUseSmallView) {
            isAnimationInProgress = true
            isSmallViewEnabled = shouldUseSmallView
            val heightSmall = resources.getDimension(R.dimen.player_height_small).toInt()
            val heightLarge = resources.getDimension(R.dimen.player_height_large).toInt()

            val heights = if (shouldUseSmallView) Pair(heightLarge, heightSmall) else Pair(heightSmall, heightLarge)
            val alphas1 = if (shouldUseSmallView) Pair(1f, 0f) else Pair(0f, 1f)
            val alphas2 = if (shouldUseSmallView) Pair(0f, 1f) else Pair(1f, 0f)

            val heightAnimator = ValueAnimator.ofInt(heights.first, heights.second)
            heightAnimator.addUpdateListener { animation ->
                run {
                    setViewHeight(player_large, animation.animatedValue as Int)
                }
            }

            val alphaAnimator1 = ValueAnimator.ofFloat(alphas1.first, alphas1.second)
            alphaAnimator1.addUpdateListener { animation ->
                run {
                    player_large.alpha = animation.animatedValue as Float
                }
            }

            val alphaAnimator2 = ValueAnimator.ofFloat(alphas2.first, alphas2.second)
            alphaAnimator2.addUpdateListener { animation ->
                run {
                    player_small.alpha = animation.animatedValue as Float
                }
            }

            val animatorSet = AnimatorSet()
            animatorSet.play(heightAnimator)
            animatorSet.play(alphaAnimator1)
            animatorSet.play(alphaAnimator2)
            animatorSet.duration = 300L
            animatorSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animator: Animator) {}

                override fun onAnimationEnd(animator: Animator) {
                    if (shouldUseSmallView) {
                        player_large.visibility = View.GONE
                    } else {
                        player_small.visibility = View.GONE
                    }
                    isAnimationInProgress = false
                }

                override fun onAnimationCancel(animator: Animator) {}

                override fun onAnimationStart(animator: Animator) {
                    if (shouldUseSmallView) {
                        player_small.alpha = 0f
                        player_small.visibility = View.VISIBLE
                    } else {
                        player_large.alpha = 0f
                        player_large.visibility = View.VISIBLE
                    }
                }
            })
            animatorSet.start()
        }
    }

    private fun setClosed(isClosed: Boolean) {
        if (!isAnimationInProgress) {
            isAnimationInProgress = true

            val height = resources.getDimension(R.dimen.player_height_large).toInt()

            val heights = if (isClosed) Pair(height, 0) else Pair(0, height)
            val alphas = if (isClosed) Pair(1f, 0f) else Pair(0f, 1f)

            val heightAnimator = ValueAnimator.ofInt(heights.first, heights.second)
            heightAnimator.addUpdateListener { animation ->
                run {
                    setViewHeight(this@PlayerView, animation.animatedValue as Int)
                }
            }

            val alphaAnimator = ValueAnimator.ofFloat(alphas.first, alphas.second)
            alphaAnimator.addUpdateListener { animation ->
                run {
                    alpha = animation.animatedValue as Float
                }
            }

            val animatorSet = AnimatorSet()
            animatorSet.play(heightAnimator)
            animatorSet.play(alphaAnimator)
            animatorSet.duration = 300L
            animatorSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animator: Animator) {}

                override fun onAnimationEnd(animator: Animator) {
                    if (isClosed) {
                        visibility = View.GONE
                    } else {
                        setViewHeight(this@PlayerView, WRAP_CONTENT)
                    }
                    isAnimationInProgress = false
                }

                override fun onAnimationCancel(animator: Animator) {}

                override fun onAnimationStart(animator: Animator) {
                    if (!isClosed) {
                        alpha = 0f
                        setViewHeight(this@PlayerView, 0)
                        visibility = View.VISIBLE
                    }
                }
            })
            animatorSet.start()
        }
    }

    private fun setViewHeight(view: View, height: Int) {
        val layoutParams = view.layoutParams
        layoutParams.height = height
        view.layoutParams = layoutParams
    }

    private fun setPlayingState(state: Boolean) {
        isPlaying = state
        val resource = if (isPlaying) R.drawable.ic_player_pause else R.drawable.ic_player_play
        player_play_large.setImageResource(resource)
        player_play_small.setImageResource(resource)
    }

    private fun onForwardClick() {
        val newPosition = player.currentPosition + SEEK_STEP
        seekTo(if (newPosition >= player.duration) player.duration else newPosition)
    }

    private fun onNextClick() {
        playAnother(1)
    }

    override fun onPrepared(p0: MediaPlayer) {
        play()
    }

    override fun onError(p0: MediaPlayer, p1: Int, p2: Int): Boolean {
        player.reset()
        return false
    }

    override fun onCompletion(p0: MediaPlayer) {
        if (isPlaying) {
            playAnother(1)
        }
    }

    override fun onBufferingUpdate(p0: MediaPlayer, p1: Int) {

    }

    private fun play() {
        if (isPrepared) {
            player.start()
            player_progress.max = player.duration
            player_time_total.text = getTiming(player.duration)
            listener.onPlayingStart(sectionId)
            updateProgressBar()
        } else {
            player.reset()
            try {
                val descriptor = resources.openRawResourceFd(
                        resources.getIdentifier(filePath, "raw", context.applicationContext.packageName)
                )
                player.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
                player.prepareAsync()
                descriptor.close()
                isPrepared = true
            } catch (e: IOException) {
                Log.w(TAG, "Unable to open: $filePath", e)
            }
        }
    }

    private fun getTiming(time: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(time.toLong()) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%1\$d:%2\$02d", minutes, seconds)
    }

    private fun playAnother(shift: Int) {
        player.reset()
        isPrepared = false
        val newSectionId = sectionId + shift
        if (newSectionId in 0 until sectionCount) {
            listener.onPlayingStop(sectionId)
            sectionId = newSectionId
            createFilePath()
            setPlayingState(true)
            play()
        }
        updateNextPrevButtons()
    }

    private fun updateNextPrevButtons() {
        if (sectionId <= 0) {
            player_previous.isEnabled = false
            player_previous.alpha = DISABLED_BUTTON_ALPHA
        } else {
            player_previous.isEnabled = true
            player_previous.alpha = ENABLED_BUTTON_ALPHA
        }
        if (sectionId >= sectionCount - 1) {
            player_next.isEnabled = false
            player_next.alpha = DISABLED_BUTTON_ALPHA
        } else {
            player_next.isEnabled = true
            player_next.alpha = ENABLED_BUTTON_ALPHA
        }
    }

    private fun updateProgressBar() {
        if (isPlaying) {
            player_progress.progress = player.currentPosition
            playerHandler.postDelayed(this::updateProgressBar, PROGRESS_UPDATE_DELAY)
        }
    }

    private fun internalPause() {
        listener.onPlayingStop(sectionId)
        player.pause()
    }

    private fun seekTo(position: Int) {
        if (position >= 0) {
            player.seekTo(position)
        }
    }

    fun destroy() {
        setPlayingState(false)
        player.reset()
    }

    fun playSection(sectionId: Int) {
        if (visibility != View.VISIBLE) {
            setClosed(false)
        }

        if (this.sectionId == sectionId) {
            onPlayClick()
        } else {
            this.sectionId = sectionId
            playAnother(0)
        }
    }

    interface OnStateChangedListener {
        fun onPlayingStart(section: Int)
        fun onPlayingStop(section: Int)
    }
}