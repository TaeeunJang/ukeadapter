package com.cooper.center.ukeadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import java.lang.reflect.Method

open class UkeBindingPresenter(
    condition: UkeCondition,
    private val resId: Int
) : UkePresenter<Any>(condition) {

    private var eventMethod: Method? = null
    private var pipeMethod: Method? = null
    private var bindingMethod: Method? = null

    override fun onCreateViewHolder(parent: ViewGroup): UkeHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(resId(), parent, false)
        val holder = UkeHolder(itemView, event())

        try {
            eventMethod = UkeHelper.bind(eventMethod, holder.binder(), event())
        } catch (ignored: Exception) {}

        try {
            pipeMethod = UkeHelper.bind(pipeMethod, holder.binder(), pipe())
        } catch (ignored: Exception) {}

        return holder
    }

    override fun onBindViewHolder(holder: UkeHolder, `object`: Any) {
        try {
            bindingMethod = UkeHelper.bind(bindingMethod, holder.binder(), `object`)
        } catch (ignored: Exception) {}
    }

    fun resId(): Int {
        return resId
    }

}
