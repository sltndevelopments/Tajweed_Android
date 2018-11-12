package ru.tajwid.app.ui.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.view_font_settings.view.*
import ru.tajwid.app.R

class FontSettingsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_font_settings, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        font_settings_seek_bar.setMax(5)
    }

    fun setOnSeekBarChangeListener(listener: SeekBar.OnSeekBarChangeListener) {
        font_settings_seek_bar.setOnSeekBarChangeListener(listener)
    }
}