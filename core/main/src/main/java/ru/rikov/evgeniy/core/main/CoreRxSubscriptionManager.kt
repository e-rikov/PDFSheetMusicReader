package ru.rikov.evgeniy.core.main

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable


interface CoreRxSubscriptionManager {
    
    fun Disposable.addToQueue(): Disposable
    
    fun clearQueue()
    
    fun dispose(reInitQueue: () -> Unit = {})
    
    fun <T> Maybe<T>.addToQueue(onSuccess: ((T) -> Unit)? = null): Disposable
    
    fun <T> Maybe<T>.addToQueue(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable
    
    fun <T> Single<T>.addToQueue(onSuccess: ((T) -> Unit)? = null): Disposable
    
    fun <T> Single<T>.addToQueue(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable
    
    fun <T> Observable<T>.addToQueue(onSuccess: ((T) -> Unit)? = null): Disposable
    
    fun <T> Observable<T>.addToQueue(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable
    
    fun Completable.addToQueue(onSuccess: (() -> Unit)? = null): Disposable
    
    fun Completable.addToQueue(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable
    
}