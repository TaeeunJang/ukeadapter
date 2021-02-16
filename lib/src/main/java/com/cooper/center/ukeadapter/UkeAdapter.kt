package com.cooper.center.ukeadapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import java.util.function.Predicate

class UkeAdapter : RecyclerView.Adapter<UkeHolder>(), MutableList<Any> {

    /** Builder **/
    class Builder {
        private val adapter: UkeAdapter = UkeAdapter()

        fun name(name: String): Builder {
            adapter.pipe.name(name)
            return this
        }

        fun binding(clz: Class<*>, resId: Int): Builder {
            return binding(UkeCondition.clz(clz), resId)
        }

        fun binding(condition: UkeCondition, resId: Int): Builder {
            val presenter = UkeBindingPresenter(condition, resId)
            presenter.event(adapter.event)
            presenter.pipe(adapter.pipe)
            adapter.presenters.add(presenter)
            return this
        }

        fun viewModel(clz: Class<*>, resId: Int, vmClz: Class<out UkeViewModel<*>>): Builder {
            return viewModel(UkeCondition.clz(clz), resId, vmClz)
        }

        fun viewModel(condition: UkeCondition, resId: Int, vmClz: Class<out UkeViewModel<*>>): Builder {
            return viewModel(condition, resId, object: UkeViewModel.Factory {
                override fun newViewModel(): UkeViewModel<*> {
                    return vmClz.newInstance()
                }
            })
        }

        fun viewModel(clz: Class<*>, resId: Int, factory: UkeViewModel.Factory): Builder {
            return viewModel(UkeCondition.clz(clz), resId, factory)
        }

        fun viewModel(condition: UkeCondition, resId: Int, factory: UkeViewModel.Factory): Builder {
            val presenter = UkeViewModelPresenter(condition, resId, factory)
            presenter.event(adapter.event)
            presenter.pipe(adapter.pipe)
            adapter.presenters.add(presenter)
            return this
        }

        fun presenter(clz: Class<*>, resId: Int, presenterClass: Class<out UkePresenter<Any>>): Builder {
            return presenter(UkeCondition.clz(clz), resId, presenterClass)
        }

        fun presenter(
            condition: UkeCondition, resId: Int,
            presenterClass: Class<out UkePresenter<Any>>
        ): Builder {
            try {
                val constructor = presenterClass
                    .getConstructor(UkeCondition::class.java, Int::class.javaPrimitiveType)
                presenter(constructor.newInstance(condition, resId))
            } catch (ignored: Exception) {
                throw FailedToCreateUkePresenter()
            }

            return this
        }

        fun presenter(presenter: UkePresenter<Any>): Builder {
            presenter.event(adapter.event)
            presenter.pipe(adapter.pipe)
            adapter.presenters.add(presenter)
            return this
        }

        fun event(event: UkeEvent): Builder {
            adapter.event = event
            return this
        }

        fun connect(pipe: UkePipe): Builder {
            adapter.pipe.connect(pipe)
            return this
        }

        fun build(): UkeAdapter {
            return adapter
        }
    }

    /** Member Variables **/
    private val objects: ArrayList<Any> = ArrayList()
    private val viewTypes: ArrayList<Int> = ArrayList()
    private var presenters: ArrayList<UkePresenter<Any>> = ArrayList()
    private var event: UkeEvent = UkeEvent(this)
    private val pipe: UkePipe = UkePipe()

    /** RecyclerView.Adapter<UkeHolder> **/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UkeHolder {
        val presenter = presenters[viewType]
        val holder = presenter.onCreateViewHolder(parent)

        holder.itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                presenter.onViewAttachedToWindow(holder)
            }

            override fun onViewDetachedFromWindow(view: View) {
                presenter.onViewDetachedFromWindow(holder)
            }
        })

        return holder
    }

    override fun onBindViewHolder(holder: UkeHolder, position: Int) {
        val `object` = objects[position]
        val viewType = viewTypes[position]
        val presenter = presenters[viewType]

        if (holder.model() != null) {
            presenter.onUnbindViewHolder(holder)
        }

        presenter.onBindViewHolder(holder, `object`)
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewTypes[position]
    }

    /** List **/
    override val size: Int
        get() = objects.size

    override fun isEmpty(): Boolean {
        return objects.isEmpty()
    }

    override fun contains(element: Any): Boolean {
        return objects.contains(element)
    }

    override fun iterator(): MutableIterator<Any> {
        throw UnsupportedOperationException()
    }

    override fun add(element: Any): Boolean {
        if (objects.add(element)) {
            val position = objects.size - 1
            viewTypes.add(getViewType(position))
            notifyItemInserted(position)
            return true
        }
        return false
    }

    override fun remove(element: Any): Boolean {
        val position = objects.indexOf(element)
        if (position < 0 || position >= objects.size) {
            return false
        }

        objects.removeAt(position)
        viewTypes.removeAt(position)

        notifyItemRemoved(position)

        return true
    }

    override fun addAll(elements: Collection<Any>): Boolean {
        val from = objects.size

        if (objects.addAll(elements)) {
            for (i in from until from + elements.size) {
                viewTypes.add(i, getViewType(i))
            }
            notifyItemRangeInserted(from, elements.size)
            return true
        }

        return false
    }

    override fun addAll(index: Int, elements: Collection<Any>): Boolean {
        if (objects.addAll(index, elements)) {
            for (i in index until index + elements.size) {
                viewTypes.add(i, getViewType(i))
            }

            notifyItemRangeInserted(index, elements.size)

            return true
        }

        return false
    }

    override fun clear() {
        if (objects.isNotEmpty()) {
            val size = objects.size

            objects.clear()
            viewTypes.clear()

            notifyItemRangeRemoved(0, size)
        }
    }

    override fun get(index: Int): Any {
        return objects[index]
    }

    override fun set(index: Int, element: Any): Any {
        val oldObject = objects.set(index, element)

        viewTypes[index] = getViewType(index)

        notifyItemChanged(index)

        return oldObject
    }

    override fun add(index: Int, element: Any) {
        objects.add(index, element)
        viewTypes.add(index, getViewType(index))

        notifyItemInserted(index)
    }

    override fun removeAt(index: Int): Any {
        val oldObject = objects[index]

        objects.removeAt(index)
        viewTypes.removeAt(index)

        notifyItemRemoved(index)

        return oldObject
    }

    override fun removeAll(elements: Collection<Any>): Boolean {
        if (objects.removeAll(elements)) {
            viewTypes.clear()

            for (i in objects.indices) {
                viewTypes.add(getViewType(i))
            }

            notifyDataSetChanged()

            return true
        }

        return false
    }

    override fun removeIf(filter: Predicate<in Any>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun indexOf(element: Any): Int {
        return objects.indexOf(element)
    }

    override fun lastIndexOf(element: Any): Int {
        return objects.lastIndexOf(element)
    }

    override fun listIterator(): MutableListIterator<Any> {
        throw UnsupportedOperationException()
    }

    override fun listIterator(index: Int): MutableListIterator<Any> {
        throw UnsupportedOperationException()
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Any> {
        val adapter = UkeAdapter()

        adapter.presenters = presenters
        adapter.objects.addAll(objects.subList(fromIndex, toIndex))
        adapter.viewTypes.addAll(viewTypes.subList(fromIndex, toIndex))

        return adapter
    }

    override fun retainAll(elements: Collection<Any>): Boolean {
        return objects.retainAll(elements)
    }

    override fun containsAll(elements: Collection<Any>): Boolean {
        return objects.containsAll(elements)
    }

    /** ViewTypes  */
    private fun getViewType(position: Int): Int {
        val `object` = objects[position]

        for (viewType in presenters.indices) {
            val presenter = presenters[viewType]

            if (presenter.accept(`object`, position, objects.size)) {
                return viewType
            }
        }

        throw FailedToFindViewType(`object`, position)
    }

    fun event() : UkeEvent {
        return event
    }

    fun pipe() : UkePipe {
        return pipe
    }

}
