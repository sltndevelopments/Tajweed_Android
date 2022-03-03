package ru.tajwid.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main_new.*
import kotlinx.android.synthetic.main.fragment_main.*
import ru.tajwid.app.R
import ru.tajwid.app.content.db.DbManager
import ru.tajwid.app.ui.view.MainMenuItemView

class MainFragment : Fragment() {

    val progressDao = DbManager.get().getProgressDAO()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onResume() {
        super.onResume()

        val (moduleTotal, moduleCompleted) = progressDao.getModulesProgressInfo()

        main_menu_container.removeAllViews()
        for (module in DbManager.get().getModulesDAO().getAll()!!) {
            val menuItem = MainMenuItemView(requireContext())
            menuItem.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            menuItem.setImage(module.id)
            menuItem.setText(module.title)
            menuItem.setDescription(
                getString(
                    R.string.main_modules_progress_info,
                    moduleCompleted[module.id],
                    moduleTotal[module.id]
                )
            )
            menuItem.setOnClickListener {
                setSelectedItem(module.id)
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.host, LessonsListFragment.newInstance(module.title, module.id))
                    .commit()
            }
            main_menu_container.addView(menuItem)
        }
    }

    private fun setSelectedItem(moduleId: Int) {
        when(moduleId) {
            0 -> requireActivity().bottom.selectedItemId = R.id.alphabet
            1 -> requireActivity().bottom.selectedItemId = R.id.pronunciation
            2 -> requireActivity().bottom.selectedItemId = R.id.reading
        }
    }
}