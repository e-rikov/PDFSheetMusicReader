package ru.rikov.evgeniy.core.android.tools

import androidx.fragment.app.Fragment

fun <T : Any> Fragment.autoCleaned(initializer: (() -> T)? = null): AutoCleanedValue<T> {
    return AutoCleanedValue(this, initializer)
}