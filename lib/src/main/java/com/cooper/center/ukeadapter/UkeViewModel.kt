package com.cooper.center.ukeadapter

import android.content.Context

abstract class UkeViewModel<Model> {

    interface Factory {
        fun newViewModel(): UkeViewModel<*>
    }

    private var model: Model? = null
    private var context: Context? = null
    private lateinit var event: UkeEvent
    private lateinit var pipe: UkePipe

    fun init(context: Context, event: UkeEvent, pipe: UkePipe) {
        this.context = context
        this.event = event
        this.pipe = pipe
    }

    @Suppress("UNCHECKED_CAST")
    fun model(model: Any) {
        this.model = model as Model
    }

    fun model(): Model {
        return model!!
    }

    fun context(): Context? {
        return context
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

    fun onCreate() {

    }

    fun onAttach() {

    }

    fun onDetach() {

    }

    fun onBind() {

    }

    fun onUnbind() {

    }

}
