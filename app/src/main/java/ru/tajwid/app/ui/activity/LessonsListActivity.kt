package ru.tajwid.app.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_lessons_list.*
import ru.tajwid.app.BuildConfig
import ru.tajwid.app.R
import ru.tajwid.app.content.db.DbManager
import ru.tajwid.app.ui.adapter.LessonsListAdapter
import ru.tajwid.app.ui.adapter.LessonsListAdapter.Item

private const val EXTRA_TITLE = "title"
private const val EXTRA_MODULE_ID = "module_id"

class LessonsListActivity : BaseActivity(), LessonsListAdapter.OnClickListener {

    private var moduleId = 0
    private var lessonId = 0
    private val progressDao = DbManager.get().getProgressDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lessons_list)

        setupSimpleToolbar(lessons_list_toolbar, intent.getStringExtra(EXTRA_TITLE))
        moduleId = intent.getIntExtra(EXTRA_MODULE_ID, 0)

        val adapter = LessonsListAdapter()
        val items = mutableListOf<Item>()
        val lesson = DbManager.get().getModulesDAO().get(moduleId)!!.lessons
        val allLessonsStates = progressDao.getAllLessonsStates(moduleId, lessonId)

        for (lessonId in 0 until lesson.size) {
            val isAvailable = isFullVersion() || lessonId < MAX_FREE_POSITION
            if (allLessonsStates[lessonId]!!) {
                val item = Item(
                    lesson = lesson[lessonId]!!,
                    isFilled = true,
                    isAvailable = isAvailable
                )
                items.add(item)
            } else {
                val item = Item(
                    lesson = lesson[lessonId]!!,
                    isFilled = false,
                    isAvailable = isAvailable
                )
                items.add(item)
            }
        }
        adapter.items = items

        lessons_list_recycler_view.adapter = adapter
        lessons_list_recycler_view.layoutManager = LinearLayoutManager(this)
        adapter.listener = this
    }

    override fun onClick(position: Int) {
        when {
            isFullVersion() -> openLesson(position)
            position < MAX_FREE_POSITION -> openLesson(position)
            else -> openFullVersion(position)
        }
    }

    private fun isFullVersion(): Boolean {
        return BuildConfig.APPLICATION_ID == FULL_VERSION_PACKAGE
    }

    private fun openFullVersion(position: Int) {
        if (isAppInstalled(FULL_VERSION_PACKAGE)) {
            packageManager.getLaunchIntentForPackage(FULL_VERSION_PACKAGE)?.let { intent ->
                startActivity(intent)
            } ?: openFullVersionInStore()
        } else {
            openFullVersionInStore()
        }
    }

    private fun openFullVersionInStore() {
        startActivity(Intent(Intent.ACTION_VIEW, FULL_VERSION_URI))
    }

    private fun isAppInstalled(fullPackage: String): Boolean {
        return try {
            packageManager.getApplicationInfo(fullPackage, 0).enabled
        } catch (e: Exception) {
            false
        }
    }

    private fun openLesson(position: Int) {
        startActivity(LessonActivity.getIntent(this, moduleId, position))
    }

    companion object {
        private const val FULL_VERSION_PACKAGE = "ru.tajwid.app.full"
        private val FULL_VERSION_URI = Uri.parse("market://details?id=$FULL_VERSION_PACKAGE")

        private const val MAX_FREE_POSITION = 2
        fun getIntent(context: Context, title: String, moduleId: Int): Intent {
            val intent = Intent(context, LessonsListActivity::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_MODULE_ID, moduleId)
            return intent
        }
    }
}
