package com.cooper.center.ukeadapter

import androidx.databinding.ViewDataBinding

class UkeViewModelHolder(val innerHolder: UkeHolder, val viewModel: UkeViewModel<*>) :
    UkeHolder(innerHolder.itemView, innerHolder.event) {

    override fun <T : ViewDataBinding> binder(ignored: Class<T>): T? {
        return innerHolder.binder(ignored)
    }

    override fun binder(): ViewDataBinding {
        return innerHolder.binder()
    }

    override fun event(): UkeEvent {
        return innerHolder.event()
    }

    override fun model(model: Any) {
        innerHolder.model(model)
    }

    override fun model(): Any? {
        return innerHolder.model()
    }

}
