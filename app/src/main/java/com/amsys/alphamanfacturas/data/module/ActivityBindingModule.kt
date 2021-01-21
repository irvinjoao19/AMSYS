package com.amsys.alphamanfacturas.data.module

import com.amsys.alphamanfacturas.ui.activities.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    internal abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [FragmentBindingModule.Main::class])
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBindingModule.FormAviso::class])
    internal abstract fun bindFormAvisoActivity(): FormAvisoActivity

    @ContributesAndroidInjector(modules = [FragmentBindingModule.FormInspeccion::class])
    internal abstract fun bindFormInspeccionActivity(): FormInspeccionActivity
}