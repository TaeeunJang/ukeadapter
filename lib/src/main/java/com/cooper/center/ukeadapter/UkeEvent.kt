package com.cooper.center.ukeadapter

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.lang.ref.WeakReference

class UkeEvent internal constructor(adapter: UkeAdapter) : Observable<Any>() {

    private val weakAdapter: WeakReference<UkeAdapter> = WeakReference(adapter)
    private val subject: PublishSubject<Any> = PublishSubject.create()

    fun publish(@NonNull event: Any) {
        if (weakAdapter.get() == null) {
            return
        }

        if (!subject.hasComplete() && !subject.hasThrowable()) {
            subject.onNext(event)
        }
    }

    fun close() {
        subject.onComplete()
    }

    /** extends Observable */
    override fun subscribeActual(observer: Observer<in Any>) {
        weakAdapter.get() ?: return

        subject.subscribe(ProxyObserver(observer))
    }

    /** class ProxyObserver  */
    private class ProxyObserver internal constructor(internal var observer: Observer<in Any>) : Observer<Any> {
        override fun onSubscribe(@NonNull d: Disposable) {
            observer.onSubscribe(d)
        }

        override fun onNext(@NonNull o: Any) {
            observer.onNext(o)
        }

        override fun onError(@NonNull e: Throwable) {
            observer.onError(e)
        }

        override fun onComplete() {
            observer.onComplete()
        }
    }

}
