package ru.rikov.evgeniy.core.android.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import ru.rikov.evgeniy.core.android.UiRxSubscriptionManagerDelegate
import ru.rikov.evgeniy.core.main.UiRxSubscriptionManager


open class BaseViewModel :
    ViewModel(),
    UiRxSubscriptionManager by UiRxSubscriptionManagerDelegate()
{

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        dispose()
    }

}