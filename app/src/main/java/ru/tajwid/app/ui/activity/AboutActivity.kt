package ru.tajwid.app.ui.activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_about.*
import ru.tajwid.app.R

private const val URI_PREFIX_HTTP = "http://"

class AboutActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, AboutActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setupSimpleToolbar(about_toolbar, getString(R.string.settings))
        go_to_the_site.setOnClickListener { onGoToTheSiteClick() }
    }

    private fun onGoToTheSiteClick() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(URI_PREFIX_HTTP + getString(R.string.settings_web_site_about_project))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}