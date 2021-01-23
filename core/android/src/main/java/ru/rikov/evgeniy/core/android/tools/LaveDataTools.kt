package ru.rikov.evgeniy.core.android.tools

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


fun <T> LiveData<T>.observeNullable(owner: LifecycleOwner, observer: (T?) -> Unit): Observer<T?> {
    val lifecycleObserver = Observer(observer)
    observe(owner, lifecycleObserver)
    return lifecycleObserver
}

fun <T> LiveData<T>.observeNotNull(owner: LifecycleOwner, observer: (T) -> Unit): Observer<T> {
    val lifecycleObserver = Observer<T> { value ->
        value?.also { observer(it) }
    }

    observe(owner, lifecycleObserver)
    return lifecycleObserver
}