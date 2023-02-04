package ru.tajwid.app.ui.fragment

import android.app.Activity
import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.activitiy_learning.*
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.fragment_language_settings.*
import ru.tajwid.app.R
import ru.tajwid.app.content.data.OnlineLearning
import java.util.*


class LanguageSettingsFragment : Fragment() {

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
        val locales = listOf(systemLocale, "ru-RU", "tr-rTR")
        spinner.adapter = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_list_item_1,
            locales
        )
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLocale = spinner.adapter.getItem(position) as String
                if (selectedLocale != systemLocale) {
                    updateAppLocales(Locale.forLanguageTag(selectedLocale))
                } else {
                    updateAppLocales()

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    fun updateActivityTitle() {
//        val localeManager = Activity().getSystemService(LocaleManager::class.java)
//        val appLocales = localeManager.applicationLocales
//        var title = if (appLocales.isEmpty) {
//            getString(R.string.system_locale)
//        } else {
//            appLocales.get(0).displayName
//        }
//    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun updateAppLocales(vararg locales: Locale) {
        val localeManager = getSystemService(LocaleManager::class.java)
        localeManager.applicationLocales = LocaleList(*locales)
    }
}