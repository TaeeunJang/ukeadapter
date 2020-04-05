package com.cooper.center.ukeadapter

import android.view.ViewGroup

abstract class UkePresenter<Model> (
    private val condition: UkeCondition
) {

    private lateinit var event: UkeEvent
    private lateinit var pipe: UkePipe

    fun accept(`object`: Any, position: Int, totalSize: Int): Boolean {
        return condition.accept(`object`, position, totalSize)
    }

    fun event(event: UkeEvent) {
        this.event = event
    }

    fun pipe(pipe: UkePipe) {
        this.pipe = pipe
    }

    fun event(): UkeEvent {
        return event
    }

    fun pipe(): UkePipe {
        return pipe
    }

    fun path(): String {
        return pipe.path()
    }

    abstract fun onCreateViewHolder(parent: ViewGroup): UkeHolder
    abstract fun onBindViewHolder(holder: UkeHolder, `object`: Model)

    open fun onUnbindViewHolder(holder: UkeHolder) {}
    open fun onViewAttachedToWindow(holder: UkeHolder) {}
    open fun onViewDetachedFromWindow(holder: UkeHolder) {}

}
