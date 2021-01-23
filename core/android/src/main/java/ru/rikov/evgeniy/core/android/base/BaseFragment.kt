package ru.rikov.evgeniy.core.android.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

import ru.rikov.evgeniy.core.android.tools.observeNotNull
import ru.rikov.evgeniy.core.android.tools.observeNullable


open class BaseFragment(
    @LayoutRes
    layoutResId: Int = 0
) : Fragment(layoutResId) {

    open val diModules: List<Module> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (diModules.isNotEmpty()) {
            loadKoinModules(diModules)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (diModules.isNotEmpty()) {
            unloadKoinModules(diModules)
        }
    }

    protected fun <T> LiveData<T>.observeNullable(observer: (T?) -> Unit): Observer<T?> =
        observeNullable(viewLifecycleOwner, observer)

    protected fun <T> LiveData<T>.observeNotNull(observer: (T) -> Unit): Observer<T> =
        observeNotNull(viewLifecycleOwner, observer)

}