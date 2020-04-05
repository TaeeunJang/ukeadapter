package com.cooper.center.ukeadapter

import android.view.ViewGroup

class UkeViewModelPresenter(
    condition: UkeCondition,
    resId: Int,
    private val factory: UkeViewModel.Factory
) : UkeBindingPresenter(condition, resId) {

    override fun onCreateViewHolder(parent: ViewGroup): UkeHolder {
        val context = parent.context
        val viewModel = factory.newViewModel()

        viewModel.init(context, event(), pipe())

        val holder = UkeViewModelHolder(
            super.onCreateViewHolder(parent),
            viewModel
        )

        viewModel.onCreate()

        return holder
    }

    override fun onBindViewHolder(holder: UkeHolder, `object`: Any) {
        val viewModelHolder = holder as UkeViewModelHolder

        viewModelHolder.viewModel.model(`object`)

        super.onBindViewHolder(holder, viewModelHolder.viewModel)

        viewModelHolder.viewModel.onBind()
    }

    override fun onUnbindViewHolder(holder: UkeHolder) {
        val viewModelHolder = holder as UkeViewModelHolder

        viewModelHolder.viewModel.onUnbind()
    }

    override fun onViewAttachedToWindow(holder: UkeHolder) {
        val viewModelHolder = holder as UkeViewModelHolder

        viewModelHolder.viewModel.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: UkeHolder) {
        val viewModelHolder = holder as UkeViewModelHolder

        viewModelHolder.viewModel.onDetach()
    }

}
