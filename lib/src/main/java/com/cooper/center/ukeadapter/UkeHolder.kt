package com.cooper.center.ukeadapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class UkeHolder(
    itemView: View,
    val event: UkeEvent
) : RecyclerView.ViewHolder(itemView) {

    private val binder: ViewDataBinding = DataBindingUtil.bind(itemView)!!
    private var model: Any? = null

    @Suppress("UNCHECKED_CAST")
    open fun <T : ViewDataBinding> binder(ignored: Class<T>): T? {
        return binder as T
    }

    open fun binder(): ViewDataBinding {
        return binder
    }

    open fun event(): UkeEvent {
        return event
    }

    open fun model(model: Any) {
        this.model = model
    }

    open fun model(): Any? {
        return model
    }

}
