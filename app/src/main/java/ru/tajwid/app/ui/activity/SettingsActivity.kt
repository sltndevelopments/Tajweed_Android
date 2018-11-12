package ru.tajwid.app.ui.activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import ru.tajwid.app.R
import ru.tajwid.app.utils.FontUtils
import ru.tajwid.app.utils.PreferencesHelper

private const val URI_PREFIX_HTTP = "http://"

class SettingsActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        FontUtils.setTextViewFont(settings_notice_info, R.font.proxima_nova_semibold)
        FontUtils.setTextViewFont(settings_communication, R.font.proxima_nova_semibold)
        FontUtils.setTextViewFont(settings_about_project, R.font.proxima_nova_semibold)
        FontUtils.setTextViewFont(settings_assessment, R.font.proxima_nova_semibold)
        FontUtils.setTextViewFont(settings_recommendation, R.font.proxima_nova_semibold)

        setupSimpleToolbar(settings_toolbar, getString(R.string.settings))

        settings_assessment.setOnClickListener { onAssessmentClick() }
        settings_communication.setOnClickListener { onCommunicationClick() }
        settings_about_project.setOnClickListener { onAboutProjectClick() }
        settings_recommendation.setOnClickListener { onRecommendationClick() }
        settings_switch.isChecked = PreferencesHelper.get().isNotificationsEnabled()
        settings_switch.setOnCheckedChangeListener { _, isChecked -> onSwitchCheckedChanged(isChecked) }
    }

    private fun onSwitchCheckedChanged(isChecked: Boolean) {
        PreferencesHelper.get().setNotificationsEnabled(isChecked)
    }

    private fun onRecommendationClick() {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_recommend_text))
            startActivity(Intent.createChooser(intent, getString(R.string.settings_recommend_chooser_title)))
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun onAboutProjectClick() {
        startActivity(AboutActivity.getIntent(this))
    }

    private fun onCommunicationClick() {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("author@tajwid.ru"))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_recommend_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_recommend_text))
            startActivity(Intent.createChooser(intent, getString(R.string.settings_recommend_chooser_title)))
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun onAssessmentClick() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(URI_PREFIX_HTTP + getString(R.string.settings_information_website))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}