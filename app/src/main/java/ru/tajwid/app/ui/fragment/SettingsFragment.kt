package ru.tajwid.app.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.tajwid.app.BuildConfig
import ru.tajwid.app.R
import ru.tajwid.app.ui.activity.AboutActivity
import ru.tajwid.app.utils.FontUtils
import ru.tajwid.app.utils.PreferencesHelper

const val PREFS_NAME = "theme_prefs"
const val KEY_THEME = "prefs.theme"
const val THEME_UNDEFINED = -1
const val THEME_LIGHT = 0
const val THEME_DARK = 1
//const val THEME_SYSTEM = 2


class SettingsFragment : Fragment() {

    private val sharedPrefs by lazy { activity?. getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FontUtils.setTextViewFont(settings_notice_info, R.font.proxima_nova_semibold)
        FontUtils.setTextViewFont(settings_communication, R.font.proxima_nova_semibold)
        FontUtils.setTextViewFont(settings_about_project, R.font.proxima_nova_semibold)
        FontUtils.setTextViewFont(settings_assessment, R.font.proxima_nova_semibold)
        FontUtils.setTextViewFont(settings_recommendation, R.font.proxima_nova_semibold)

        settings_assessment.setOnClickListener { onAssessmentClick() }
        settings_communication.setOnClickListener { onCommunicationClick() }
        settings_about_project.setOnClickListener { onAboutProjectClick() }
        settings_recommendation.setOnClickListener { onRecommendationClick() }
        settings_switch.isChecked = PreferencesHelper.get().isNotificationsEnabled()

        settings_switch.setOnCheckedChangeListener { _, isChecked ->
            onSwitchCheckedChanged(
                isChecked
            )


        }
        initThemeListener()
        initTheme()
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
    }

    private fun onSwitchCheckedChanged(isChecked: Boolean) {
        PreferencesHelper.get().setNotificationsEnabled(isChecked)
    }

    private fun onRecommendationClick() {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "${getString(R.string.settings_recommend_text)}\n\n$MARKET_URL"
            )
            startActivity(
                Intent.createChooser(
                    intent,
                    getString(R.string.settings_recommend_chooser_title)
                )
            )
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun onAboutProjectClick() {
        startActivity(AboutActivity.getIntent(requireContext()))
    }


    private fun onCommunicationClick() {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("author@tajwid.ru"))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_recommend_subject))
            startActivity(
                Intent.createChooser(
                    intent,
                    getString(R.string.settings_recommend_chooser_title)
                )
            )
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun onAssessmentClick() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(MARKET_URL)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val MARKET_URL =
            "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
    }

    private fun initThemeListener(){
        themeGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.themeLight -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
                R.id.themeDark -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
//                R.id.themeSystem -> setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, THEME_SYSTEM)
            }
        }
    }

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }

    private fun initTheme() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
//            themeSystem.visibility = View.VISIBLE
//        } else {
//            themeSystem.visibility = View.GONE
//        }
        when (getSavedTheme()) {
            THEME_LIGHT -> themeLight.isChecked = true
            THEME_DARK -> themeDark.isChecked = true
//            THEME_SYSTEM -> themeSystem.isChecked = true

            THEME_UNDEFINED -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> themeLight.isChecked = true
                    Configuration.UI_MODE_NIGHT_YES -> themeDark.isChecked = true
//                    Configuration.UI_MODE_NIGHT_UNDEFINED -> themeLight.isChecked = true
                }
            }
        }
    }

    private fun saveTheme(theme: Int) = sharedPrefs?.edit()?.putInt(KEY_THEME, theme)?.apply()

    private fun getSavedTheme() = sharedPrefs?.getInt(KEY_THEME, THEME_UNDEFINED)
}