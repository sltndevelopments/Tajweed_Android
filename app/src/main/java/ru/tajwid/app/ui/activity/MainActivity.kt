package ru.tajwid.app.ui.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*
import ru.tajwid.app.R
import ru.tajwid.app.content.db.DbManager
import ru.tajwid.app.receiver.NotificationsAlarmReceiver
import ru.tajwid.app.ui.view.MainMenuItemView
import ru.tajwid.app.utils.FontUtils
import ru.tajwid.app.utils.JsonImportHelper
import ru.tajwid.app.utils.PreferencesHelper
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    val progressDao = DbManager.get().getProgressDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FontUtils.setTextViewFont(author_project, R.font.proxima_nova_semibold)
        FontUtils.setTextViewFont(main_total_reading_progress_info, R.font.proxima_nova_semibold)
        FontUtils.setTextViewFont(settings, FontUtils.getRegularTypefaceResId())

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        calendar.set(Calendar.HOUR_OF_DAY, 19)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        PreferencesHelper.get().setNextNotificationTime(calendar.timeInMillis)

        JsonImportHelper.import(this)
        NotificationsAlarmReceiver.schedule(this)

        main_settings.setOnClickListener { onSettingsClick() }
    }

    override fun onResume() {
        super.onResume()
        val (totalTotal, totalCompleted) = progressDao.getTotalProgressInfo()
        main_total_reading_progress.progress = totalCompleted * 100 / totalTotal
        main_total_reading_progress_info.text = resources.getQuantityString(
            R.plurals.main_total_reading_progress_info,
            totalCompleted,
            totalCompleted,
            totalTotal
        )

        val (moduleTotal, moduleCompleted) = progressDao.getModulesProgressInfo()

        main_menu_container.removeAllViews()
        for (module in DbManager.get().getModulesDAO().getAll()!!) {
            val menuItem = MainMenuItemView(this)
            menuItem.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            menuItem.setText(module.title)
            menuItem.setDescription(
                getString(
                    R.string.main_modules_progress_info,
                    moduleCompleted[module.id],
                    moduleTotal[module.id]
                )
            )
            menuItem.setListener(View.OnClickListener {
                startActivity(
                    LessonsListActivity.getIntent(
                        this,
                        module.title,
                        module.id
                    )
                )
            })
            main_menu_container.addView(menuItem)
        }
    }

    private fun onSettingsClick() {
        startActivity(SettingsActivity.getIntent(this))
    }
}