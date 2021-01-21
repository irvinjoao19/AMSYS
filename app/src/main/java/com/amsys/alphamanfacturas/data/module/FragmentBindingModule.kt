package com.amsys.alphamanfacturas.data.module

import com.amsys.alphamanfacturas.ui.fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

abstract class FragmentBindingModule {

    @Module
    abstract class Main {
        @ContributesAndroidInjector
        internal abstract fun providMainFragment(): MainFragment

        @ContributesAndroidInjector
        internal abstract fun providAvisosFragment(): AvisosFragment

        @ContributesAndroidInjector
        internal abstract fun providInspeccionFragment(): InspeccionFragment
    }

    @Module
    abstract class FormAviso {
        @ContributesAndroidInjector
        internal abstract fun providAviso1Fragment(): Aviso1Fragment

        @ContributesAndroidInjector
        internal abstract fun providAviso2Fragment(): Aviso2Fragment

        @ContributesAndroidInjector
        internal abstract fun providAviso3Fragment(): Aviso3Fragment

        @ContributesAndroidInjector
        internal abstract fun providAviso4Fragment(): Aviso4Fragment

        @ContributesAndroidInjector
        internal abstract fun providAviso5Fragment(): Aviso5Fragment
    }

    @Module
    abstract class FormInspeccion {
        @ContributesAndroidInjector
        internal abstract fun providInspeccion1Fragment(): Inspeccion1Fragment

        @ContributesAndroidInjector
        internal abstract fun providInspeccion2Fragment(): Inspeccion2Fragment

        @ContributesAndroidInjector
        internal abstract fun providInspeccion3Fragment(): Inspeccion3Fragment
    }
}