package ru.tajwid.app.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.tajwid.app.R
import ru.tajwid.app.ui.activity.showMessage
import ru.tajwid.app.utils.NetworkService


class RegisterFragment : DialogFragment(R.layout.fragment_register) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register.setOnClickListener {
            val emailText = email.text.toString()
            val phoneText = phone.text.toString()
            val nameText = name.text.toString()

            email.addTextChangedListener {
                email_layout.setErrorMessage(
                    if (it.isNullOrBlank()) getString(R.string.error_empty_email) else null
                )
            }
            phone.addTextChangedListener {
                phone_layout.setErrorMessage(
                    if (it.isNullOrBlank()) getString(R.string.error_empty_phone) else null
                )
            }
            name.addTextChangedListener {
                name_layout.setErrorMessage(
                    if (it.isNullOrBlank()) getString(R.string.error_empty_name) else null
                )
            }

            if (emailText.isBlank() || phoneText.isBlank() || nameText.isBlank()) {
                if (emailText.isBlank()) email_layout.setErrorMessage(getString(R.string.error_empty_email))
                if (phoneText.isBlank()) phone_layout.setErrorMessage(getString(R.string.error_empty_phone))
                if (nameText.isBlank()) name_layout.setErrorMessage(getString(R.string.error_empty_name))
                return@setOnClickListener
            }

            setEnable(false)

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val resp = NetworkService.sendMessage(
                        nameText,
                        phoneText,
                        emailText,
                        need_individual.isChecked
                    )

                    if (resp?.result == true) {
                        dismiss()
                    } else {
                        throw IllegalStateException(getString(R.string.unknown_error))
                    }

                } catch (e: Throwable) {
                    e.printStackTrace()
                    launch(Dispatchers.Main) {
                        (requireActivity() as? AppCompatActivity)?.showMessage(e.message.orEmpty())
                        setEnable(true)
                    }
                }
            }
        }
        clickText_secure_confidentiality.setOnClickListener { onConfidentialityClick() }

        checkbox_secure_confidentiality.setOnCheckedChangeListener { buttonView, isChecked ->
            register.isEnabled = isChecked
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        }
    }

    private fun TextInputLayout.setErrorMessage(errorText: String? = null) {
        error = errorText
        isErrorEnabled = errorText != null
    }

    private fun setEnable(isEnable: Boolean) {
        register.isEnabled = isEnable
        name.isEnabled = isEnable
        phone.isEnabled = isEnable
        email.isEnabled = isEnable
        need_individual.isEnabled = isEnable
        checkbox_secure_confidentiality.isEnabled = isEnable
        clickText_secure_confidentiality.isEnabled = isEnable
    }

    private fun onConfidentialityClick() {
        val url = getString(R.string.url_secure_confidentiality)
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

}