package com.example.anthonyvannoppen.androidproject.injection.component

import com.example.anthonyvannoppen.androidproject.injection.module.NetworkModule
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel
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