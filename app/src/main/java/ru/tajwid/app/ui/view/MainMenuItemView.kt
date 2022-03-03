package ru.tajwid.app.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_main_menu_item.view.*
import ru.tajwid.app.R

class MainMenuItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_main_menu_item, this)
    }

    fun setImage(id: Int) {
        when (id) {
            0 -> main_menu_image.setImageResource(R.drawable.ic_alphabet)
            1 -> main_menu_image.setImageResource(R.drawable.ic_pronunciation)
            2 -> main_menu_image.setImageResource(R.drawable.ic_reading)
        }
    }

    fun setText(text: String) {
        main_menu_text.text = text
    }

    fun setDescription(description: String) {
        main_menu_description.text = description
    }
}