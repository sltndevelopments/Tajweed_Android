package ru.tajwid.app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.browser.browseractions.BrowserActionsIntent.KEY_TITLE
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_dialog_language.view.*
import ru.tajwid.app.R
import ru.tajwid.app.ui.activity.OnlineLearningActivity


class LanguageDialog : DialogFragment() {
    val TAG: String = LanguageDialog::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_language, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupClickListeners(view)

    }

    override fun onStart() {
        super.onStart()
//        dialog?.window?.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
    }

    private fun setupView(view: View) {
//        view.tvTitle.text = arguments?.getString(KEY_TITLE)
//        view.tvSubTitle.text = arguments?.getString(KEY_SUBTITLE)
    }

    private fun setupClickListeners(view: View) {
        view.buttonYES.setOnClickListener {
            // TODO: Do some task here

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.host, LanguageSettingsFragment())
            transaction?.disallowAddToBackStack()
            transaction?.commit()


            dismiss()
        }
        view.buttonNO.setOnClickListener {
            // TODO: Do some task here
            dismiss()
        }
    }

}