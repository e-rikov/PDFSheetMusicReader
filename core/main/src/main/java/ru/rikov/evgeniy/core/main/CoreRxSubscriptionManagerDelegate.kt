package ru.rikov.evgeniy.core.main

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


open class CoreRxSubscriptionManagerDelegate: CoreRxSubscriptionManager {
    
    protected var subscriptions = CompositeDisposable()
    
    
    override fun Disposable.addToQueue(): Disposable {
        subscriptions.add(this)
        return this
    }
    
    override fun clearQueue() {
        dispose { subscriptions = CompositeDisposable() }
    }
    
    /**
     * Unsubscribe and drop all queue
     */
    override fun dispose(reInitQueue: () -> Unit) {
        if (!subscriptions.isDisposed) {
            subscriptions.dispose()
            reInitQueue()
        }
    }
    
    override fun <T> Maybe<T>.addToQueue(onSuccess: ((T) -> Unit)?): Disposable {
        val disposable = if (onSuccess != null) {
            subscribe(onSuccess)
        } else {
            subscribe()
        }
        
        subscriptions.add(disposable)
        return disposable
    }
    
    override fun <T> Maybe<T>.addToQueue(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable {
        val disposable = subscribe(onSuccess, onError)
        subscriptions.add(disposable)
        return disposable
    }
    
    override fun <T> Single<T>.addToQueue(onSuccess: ((T) -> Unit)?): Disposable {
        val disposable = if (onSuccess != null) {
            subscribe(onSuccess)
        } else {
            subscribe()
        }
        
        subscriptions.add(disposable)
        return disposable
    }
    
    override fun <T> Single<T>.addToQueue(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable {
        val disposable = subscribe(onSuccess, onError)
        subscriptions.add(disposable)
        return disposable
    }
    
    override fun <T> Observable<T>.addToQueue(onSuccess: ((T) -> Unit)?): Disposable {
        val disposable = if (onSuccess != null) {
            subscribe(onSuccess)
        } else {
            subscribe()
        }
        
        subscriptions.add(disposable)
        return disposable
    }
    
    override fun <T> Observable<T>.addToQueue(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable {
        val disposable = subscribe(onSuccess, onError)
        subscriptions.add(disposable)
        return disposable
    }
    
    override fun Completable.addToQueue(onSuccess: (() -> Unit)?): Disposable {
        val disposable = if (onSuccess != null) {
            subscribe(onSuccess)
        } else {
            subscribe()
        }
        
        subscriptions.add(disposable)
        return disposable
    }
    
    override fun Completable.addToQueue(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable {
        val disposable = subscribe(onSuccess, onError)
        subscriptions.add(disposable)
        return disposable
    }

}