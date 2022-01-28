package ru.tajwid.app.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_lessons_list.*
import ru.tajwid.app.BuildConfig
import ru.tajwid.app.R
import ru.tajwid.app.content.db.DbManager
import ru.tajwid.app.ui.activity.LessonActivity
import ru.tajwid.app.ui.adapter.LessonsListAdapter
import ru.tajwid.app.ui.adapter.LessonsListAdapter.Item

private const val EXTRA_TITLE = "title"
private const val EXTRA_MODULE_ID = "module_id"

class LessonsListFragment : Fragment(), LessonsListAdapter.OnClickListener {

    private var moduleId = 0
    private var lessonId = 0
    private val progressDao = DbManager.get().getProgressDAO()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_lessons_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moduleId = requireArguments().getInt(EXTRA_MODULE_ID, 0)

        lessons_list_toolbar.title = requireArguments().getString(EXTRA_TITLE)
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
        lessons_list_recycler_view.layoutManager = LinearLayoutManager(requireContext())
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
            requireActivity().packageManager.getLaunchIntentForPackage(FULL_VERSION_PACKAGE)
                ?.let { intent ->
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
            requireActivity().packageManager.getApplicationInfo(fullPackage, 0).enabled
        } catch (e: Exception) {
            false
        }
    }

    private fun openLesson(position: Int) {
        startActivity(LessonActivity.getIntent(requireContext(), moduleId, position))
    }

    companion object {
        private const val FULL_VERSION_PACKAGE = "ru.tajwid.app.full"
        private val FULL_VERSION_URI = Uri.parse("market://details?id=$FULL_VERSION_PACKAGE")

        private const val MAX_FREE_POSITION = 2

        fun newInstance(title: String, moduleId: Int): LessonsListFragment {
            return LessonsListFragment().apply {
                arguments = bundleOf(EXTRA_MODULE_ID to moduleId, EXTRA_TITLE to title)
            }
        }
    }
}
