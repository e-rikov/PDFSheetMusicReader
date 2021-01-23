package ru.rikov.evgeniy.core.android.tools


@Suppress("UNCHECKED_CAST")
fun <T : Any> Any.cast() = this as T

@Suppress("UNCHECKED_CAST")
fun <T : Any> Any?.optCast() = this as? T