package ru.rikov.evgeniy.core.main

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable


interface UiRxSubscriptionManager: CoreRxSubscriptionManager {
    
    fun <T> Observable<T>.subscribeUi(onSuccess: ((T) -> Unit)? = null): Disposable
    
    fun <T> Observable<T>.subscribeUi(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable
    
    fun <T> Maybe<T>.subscribeUi(onSuccess: ((T) -> Unit)? = null): Disposable
    
    fun <T> Maybe<T>.subscribeUi(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable
    
    fun <T> Single<T>.subscribeUi(onSuccess: ((T) -> Unit)? = null): Disposable
    
    fun <T> Single<T>.subscribeUi(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable
    
    fun Completable.subscribeUi(onSuccess: (() -> Unit)? = null): Disposable
    
    fun Completable.subscribeUi(
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable
    
}