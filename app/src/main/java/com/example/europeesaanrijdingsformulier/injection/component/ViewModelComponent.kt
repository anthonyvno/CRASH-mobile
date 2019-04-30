package com.example.europeesaanrijdingsformulier.injection.component

import com.example.europeesaanrijdingsformulier.injection.module.NetworkModule
import com.example.europeesaanrijdingsformulier.ui.HubViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
/**
 * All modules that are required to perform the injections into the listed objects should be listed
 * in this annotation
 */
@Component(modules = [NetworkModule::class])
interface ViewModelComponent {

    /**
     * Injects the dependencies into the specified viewmodel*/
    fun inject(hubViewModel: HubViewModel)
}