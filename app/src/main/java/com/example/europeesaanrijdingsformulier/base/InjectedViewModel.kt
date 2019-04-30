package com.example.europeesaanrijdingsformulier.base

import android.arch.lifecycle.ViewModel
import com.example.europeesaanrijdingsformulier.injection.component.DaggerViewModelComponent
import com.example.europeesaanrijdingsformulier.injection.component.ViewModelComponent
import com.example.europeesaanrijdingsformulier.injection.module.NetworkModule
import com.example.europeesaanrijdingsformulier.ui.HubViewModel

abstract class InjectedViewModel : ViewModel() {
    /**
     * An ViewModelComponent is required to do the actual injecting.
     * Every Component has a default builder to which you can add all
     * modules that will be needed for the injection.
     */
    private val injector: ViewModelComponent = DaggerViewModelComponent
        .builder()
        .networkModule(NetworkModule)
        .build()

    /**
     * Perform the injection when the ViewModel is created
     */
    init {
        inject()
    }

    /**
     * Injects the required dependencies.
     * We need the 'when(this)' construct for each new ViewModel as the 'this' reference should
     * refer to an instance of that specific ViewModel.
     * Just injecting into a generic InjectedViewModel is not specific enough for Dagger.
     */
    private fun inject() {
        when (this) {
            is HubViewModel -> injector.inject(this)
        }
    }

}