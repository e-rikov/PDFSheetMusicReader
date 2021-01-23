package ru.rikov.evgeniy.core.android

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.rikov.evgeniy.core.main.CoreRxSubscriptionManagerDelegate
import ru.rikov.evgeniy.core.main.UiRxSubscriptionManager


class UiRxSubscriptionManagerDelegate:
    CoreRxSubscriptionManagerDelegate(),
    UiRxSubscriptionManager {
    
    override fun <T> Observable<T>.subscribeUi(onSuccess: ((T) -> Unit)?): Disposable =
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .addToQueue(onSuccess)
    
    override fun <T> Observable<T>.subscribeUi(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable =
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .addToQueue(onSuccess, onError)
    
    override fun <T> Maybe<T>.subscribeUi(onSuccess: ((T) -> Unit)?): Disposable =
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .addToQueue(onSuccess)
    
    override fun <T> Maybe<T>.subscribeUi(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable {
        val disposable = this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
        
        subscriptions.add(disposable)
        return disposable
    }
    
    override fun <T> Single<T>.subscribeUi(onSuccess: ((T) -> Unit)?): Disposable =
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .addToQueue(onSuccess)
    
    override fun <T> Single<T>.subscribeUi(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable =
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .addToQueue(onSuccess, onError)
    
    override fun Completable.subscribeUi(onSuccess: (() -> Unit)?): Disposable =
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .addToQueue(onSuccess)
    
    override fun Completable.subscribeUi(
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable =
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .addToQueue(onComplete, onError)
    
}