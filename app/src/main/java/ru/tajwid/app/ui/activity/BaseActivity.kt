package ru.tajwid.app.ui.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.tajwid.app.R

/**
 * Created by BArtWell on 17.02.2018.
 */
abstract class BaseActivity : AppCompatActivity() {

    fun setupSimpleToolbar(toolbar: Toolbar, title: String?) {
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.title = title
    }
}