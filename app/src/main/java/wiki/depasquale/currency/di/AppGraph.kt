package wiki.depasquale.currency.di

import android.content.Context
import spike.lifecycle.viewmodel.ViewModelEntryPoint
import wiki.depasquale.currency.App

val Context.appGraph: AppEntryPoint
    get() = (applicationContext as App).graph

val ViewModelEntryPoint.viewModelEntryPointFactory: ViewModelEntryPoint.Factory
    get() = SharedViewModelEntryPointFactory(this)
