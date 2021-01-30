package com.amsys.alphamanfacturas.data.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amsys.alphamanfacturas.data.viewModel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UsuarioViewModel::class)
    internal abstract fun bindUserViewModel(usuarioViewModel: UsuarioViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AvisoViewModel::class)
    internal abstract fun bindAvisoViewModel(avisoViewModel: AvisoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InspeccionViewModel::class)
    internal abstract fun bindInspeccionViewModel(inspeccionViewModel: InspeccionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReporteViewModel::class)
    internal abstract fun bindReporteViewModel(reporteViewModel: ReporteViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}