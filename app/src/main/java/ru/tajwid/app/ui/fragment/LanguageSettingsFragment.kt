package ru.tajwid.app.ui.fragment

import android.app.Activity
import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activitiy_learning.*
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.fragment_language_settings.*
import ru.tajwid.app.R
import java.util.*


class LanguageSettingsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            updateActivityTitle()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_language_settings, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        language_settings_toolbar.setNavigationIcon(R.drawable.ic_toolbar_back) // need to set the icon here to have a navigation icon. You can simple create an vector image by "Vector Asset" and using here
        language_settings_toolbar.setTitle(R.string.settings)// name
        language_settings_toolbar.setNavigationOnClickListener {
            // do something when click navigation
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.host, SettingsFragment())
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }

        //    * Выбор языка*

        val systemLocale = getString(R.string.system_locale)
        val spinner: Spinner = view.findViewById(R.id.localePicker)
        val locales = listOf(systemLocale, "ru", "tr")
        spinner.adapter = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_list_item_1,
            locales
        )
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLocale = spinner.adapter.getItem(position) as String
                if (selectedLocale != systemLocale) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        updateAppLocales(Locale.forLanguageTag(selectedLocale))
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        updateAppLocales()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


 private fun updateActivityTitle() {
        val localeManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Activity().getSystemService(LocaleManager::class.java)
        } else {
            TODO("VERSION.SDK_INT < TIRAMISU")
        }
        val appLocales = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            localeManager.applicationLocales
        } else {
            TODO("VERSION.SDK_INT < TIRAMISU")
        }
        if (appLocales.isEmpty) {
            getString(R.string.system_locale)
        } else {
            appLocales.get(0).displayName
        }
    }



    fun updateAppLocales(vararg locales: Locale) {
        val localeManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Activity().getSystemService(LocaleManager::class.java)
        } else {
            TODO("VERSION.SDK_INT < TIRAMISU")
        }
        localeManager.applicationLocales = LocaleList(*locales)
    }
}