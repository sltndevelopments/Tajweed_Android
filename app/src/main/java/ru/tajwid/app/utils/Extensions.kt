package ru.tajwid.app.utils

import androidx.fragment.app.DialogFragment

inline fun <reified T> DialogFragment.getDialogListener(): T? {
    return parentFragment as? T ?: activity as? T
}