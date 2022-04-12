package ru.tajwid.app.ui.activity

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
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

fun AppCompatActivity.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.showMessage(@StringRes messageRes: Int) {
    Toast.makeText(this, getString(messageRes), Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.openDialog(fragment: DialogFragment) {
    val tag = fragment::class.simpleName
    supportFragmentManager.findFragmentByTag(tag) ?: fragment.show(supportFragmentManager, tag)
}