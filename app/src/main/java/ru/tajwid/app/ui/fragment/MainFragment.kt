package ru.tajwid.app.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main_new.*
import kotlinx.android.synthetic.main.fragment_main.*
import ru.tajwid.app.R
import ru.tajwid.app.content.db.DbManager
import ru.tajwid.app.ui.activity.OnlineLearningActivity
import ru.tajwid.app.ui.view.MainMenuItemView
import java.util.*

class MainFragment : Fragment() {

    val progressDao = DbManager.get().getProgressDAO()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val (moduleTotal, moduleCompleted) = progressDao.getModulesProgressInfo()

//      //Start dialog_language
//        val dialogFrg= LanguageDialog()
//        val bundle = Bundle()
//        dialogFrg.arguments = bundle
//        val ft = Objects.requireNonNull<FragmentManager>(fragmentManager).beginTransaction()
//        dialogFrg.show(ft, dialogFrg.TAG)
//
//      //***

        go_to_online.setOnClickListener {
            val intent = Intent(activity, OnlineLearningActivity::class.java)
            startActivity(intent)
        }

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