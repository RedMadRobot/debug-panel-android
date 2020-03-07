package com.redmadrobot.debug_panel.extension

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@MainThread
inline fun <reified VM : ViewModel> Fragment.obtainViewModel(
    crossinline viewModelProducer: () -> VM
): VM {
    val factory = getViewModelFactory(viewModelProducer)
    val viewModelProvider = ViewModelProvider(this, factory)
    return viewModelProvider[VM::class.java]
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.obtainShareViewModel(
    crossinline viewModelProducer: () -> VM
): VM {
    val factory = getViewModelFactory(viewModelProducer)
    val viewModelProvider = ViewModelProvider(this.requireActivity(), factory)
    return viewModelProvider[VM::class.java]
}

inline fun <reified VM : ViewModel> getViewModelFactory(
    crossinline viewModelProducer: () -> VM
): ViewModelProvider.Factory {

    return object : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>) = viewModelProducer() as VM
    }
}

inline fun <reified T : Any, reified L : LiveData<T>> Fragment.observe(
    liveData: L,
    noinline block: (T) -> Unit
) {
    liveData.observe(viewLifecycleOwner, Observer<T> { it?.let { block.invoke(it) } })
}
