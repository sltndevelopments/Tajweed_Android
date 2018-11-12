package ru.tajwid.app.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_main_menu_item.view.*
import ru.tajwid.app.R
import ru.tajwid.app.utils.FontUtils

class MainMenuItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_main_menu_item, this)
    }

    fun setText(text: String) {
        main_menu_text.text = text
        FontUtils.setTextViewFont(main_menu_text, R.font.montserrat_regular)
    }

    fun setDescription(description: String) {
        main_menu_description.text = description
        FontUtils.setTextViewFont(main_menu_text, R.font.proxima_nova_semibold)
    }

    fun setListener(listener: OnClickListener) {
        main_menu_button.setOnClickListener(listener)
    }
}