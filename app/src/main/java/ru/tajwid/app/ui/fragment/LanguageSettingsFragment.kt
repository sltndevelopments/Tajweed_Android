package ru.tajwid.app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activitiy_learning.*
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.fragment_language_settings.*
import ru.tajwid.app.R
import ru.tajwid.app.content.data.OnlineLearning

class LanguageSettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


      }

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


    }

}