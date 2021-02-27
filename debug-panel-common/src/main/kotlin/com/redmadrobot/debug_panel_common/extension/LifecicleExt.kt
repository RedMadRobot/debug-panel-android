package com.redmadrobot.debug_panel_common.extension

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@MainThread
public inline fun <reified VM : ViewModel> Fragment.obtainViewModel(
    crossinline viewModelProducer: () -> VM
): VM {
    val factory =
        getViewModelFactory(viewModelProducer)
    val viewModelProvider = ViewModelProvider(this, factory)
    return viewModelProvider[VM::class.java]
}

@MainThread
public inline fun <reified VM : ViewModel> Fragment.obtainShareViewModel(
    crossinline viewModelProducer: () -> VM
): VM {
    val factory =
        getViewModelFactory(viewModelProducer)
    val viewModelProvider = ViewModelProvider(this.requireActivity(), factory)
    return viewModelProvider[VM::class.java]
}

public inline fun <reified VM : ViewModel> getViewModelFactory(
    crossinline viewModelProducer: () -> VM
): ViewModelProvider.Factory {

    return object : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>) = viewModelProducer() as VM
    }
}

public inline fun <reified T : Any, reified L : LiveData<T>> Fragment.observe(
    liveData: L,
    noinline block: (T) -> Unit
) {
    liveData.observe(viewLifecycleOwner, { it?.let { block.invoke(it) } })
}
