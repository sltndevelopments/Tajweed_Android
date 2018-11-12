package ru.tajwid.app.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_lessons_list.*
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

    companion object {

        fun getIntent(context: Context, title: String, moduleId: Int): Intent {
            val intent = Intent(context, LessonsListActivity::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_MODULE_ID, moduleId)
            return intent
        }
    }

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
            if (allLessonsStates[lessonId]!!) {
                val item = Item(lesson[lessonId]!!, true)
                items.add(item)
            } else {
                val item = Item(lesson[lessonId]!!, false)
                items.add(item)
            }
        }
        adapter.items = items

        lessons_list_recycler_view.adapter = adapter
        lessons_list_recycler_view.layoutManager = LinearLayoutManager(this)
        adapter.listener = this

    }

    override fun onClick(adapterPosition: Int) {
        startActivity(LessonActivity.getIntent(this, moduleId, adapterPosition))
    }
}
