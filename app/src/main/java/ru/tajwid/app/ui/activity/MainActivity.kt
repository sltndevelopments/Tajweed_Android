package ru.tajwid.app.ui.activity

import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main_new.*
import ru.tajwid.app.R
import ru.tajwid.app.content.db.DbManager
import ru.tajwid.app.ui.fragment.LanguageDialog
import ru.tajwid.app.ui.fragment.LessonsListFragment
import ru.tajwid.app.ui.fragment.MainFragment
import ru.tajwid.app.ui.fragment.SettingsFragment
import java.util.*


class MainActivity : AppCompatActivity() {
    private val modules by lazy {
        DbManager.get().getModulesDAO().getAll() ?: emptyList()
    }

    private fun openLessons(module: Int): LessonsListFragment? =
        if (modules.size > module)
            LessonsListFragment.newInstance(modules[module].title, modules[module].id)
        else null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new)

        bottom.setOnItemSelectedListener {
            return@setOnItemSelectedListener when (it.itemId) {
                R.id.main -> MainFragment()
                R.id.alphabet -> openLessons(0)
                R.id.pronunciation -> openLessons(1)
                R.id.reading -> openLessons(2)
                else -> SettingsFragment()
            }?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.host, it)
                    .commit()
                true
            } ?: false
        }

        if (savedInstanceState == null) {
            bottom.selectedItemId = R.id.main
        }

        bottom.setOnItemReselectedListener { }

    }

}