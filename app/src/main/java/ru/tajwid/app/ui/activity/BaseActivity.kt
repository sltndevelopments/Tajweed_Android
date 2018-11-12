package ru.tajwid.app.ui.activity

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import ru.tajwid.app.R

/**
 * Created by BArtWell on 17.02.2018.
 */
abstract class BaseActivity : AppCompatActivity() {

    fun setupSimpleToolbar(toolbar: Toolbar, title: String) {
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.title = title
    }
}