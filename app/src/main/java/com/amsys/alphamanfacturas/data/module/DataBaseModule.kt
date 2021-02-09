package com.amsys.alphamanfacturas.data.module

import android.app.Application
import androidx.room.Room
import com.amsys.alphamanfacturas.data.local.dao.*
import com.amsys.alphamanfacturas.data.local.AppDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Provides
    @Singleton
    internal fun provideRoomDatabase(application: Application): AppDataBase {
        if (AppDataBase.INSTANCE == null) {
            synchronized(AppDataBase::class.java) {
                if (AppDataBase.INSTANCE == null) {
                    AppDataBase.INSTANCE = Room.databaseBuilder(
                        application.applicationContext,
                        AppDataBase::class.java, AppDataBase.DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }
        return AppDataBase.INSTANCE!!
    }

    @Provides
    internal fun provideUsuarioDao(appDataBase: AppDataBase): UsuarioDao {
        return appDataBase.usuarioDao()
    }

    @Provides
    internal fun provideAvisoDao(appDataBase: AppDataBase): AvisoDao {
        return appDataBase.avisoDao()
    }

    @Provides
    internal fun provideConsecuenciaDao(appDataBase: AppDataBase): ConsecuenciaDao {
        return appDataBase.consecuenciaDao()
    }

    @Provides
    internal fun providePrioridadDao(appDataBase: AppDataBase): PrioridadDao {
        return appDataBase.prioridadDao()
    }

    @Provides
    internal fun provideParadaDao(appDataBase: AppDataBase): ParadaDao {
        return appDataBase.paradaDao()
    }

    @Provides
    internal fun provideDeteccionDao(appDataBase: AppDataBase): DeteccionDao {
        return appDataBase.deteccionDao()
    }

    @Provides
    internal fun provideMecanismoFallaDao(appDataBase: AppDataBase): MecanismoFallaDao {
        return appDataBase.mecanismoFallaDao()
    }

    @Provides
    internal fun provideImpactoDao(appDataBase: AppDataBase): ImpactoDao {
        return appDataBase.impactoDao()
    }

    @Provides
    internal fun provideCausaFallaDao(appDataBase: AppDataBase): CausaFallaDao {
        return appDataBase.causaFallaDao()
    }

    @Provides
    internal fun provideRegistroDao(appDataBase: AppDataBase): RegistroDao {
        return appDataBase.registroDao()
    }

    @Provides
    internal fun provideAvisoFileDao(appDataBase: AppDataBase): AvisoFileDao {
        return appDataBase.avisoFileDao()
    }


    @Provides
    internal fun provideEquipoDao(appDataBase: AppDataBase): EquipoDao {
        return appDataBase.equipoDao()
    }

    @Provides
    internal fun provideModoFallaDao(appDataBase: AppDataBase): ModoFallaDao {
        return appDataBase.modoFallaDao()
    }

    @Provides
    internal fun provideTipoParadaDao(appDataBase: AppDataBase): TipoParadaDao {
        return appDataBase.tipoParadaDao()
    }

    @Provides
    internal fun provideSubTipoParadaDao(appDataBase: AppDataBase): SubTipoParadaDao {
        return appDataBase.subTipoParadaDao()
    }

    @Provides
    internal fun provideInspeccionDao(appDataBase: AppDataBase): InspeccionDao {
        return appDataBase.inspeccionDao()
    }

    @Provides
    internal fun providePuntoMedidaDao(appDataBase: AppDataBase): PuntoMedidaDao {
        return appDataBase.puntoMedidaDao()
    }

    @Provides
    internal fun provideAspectoDao(appDataBase: AppDataBase): AspectoDao {
        return appDataBase.aspectoDao()
    }

    @Provides
    internal fun provideContadorDao(appDataBase: AppDataBase): ContadorDao {
        return appDataBase.contadorDao()
    }

    @Provides
    internal fun provideTalleResponsableDao(appDataBase: AppDataBase): TalleResponsableDao {
        return appDataBase.talleResponsableDao()
    }

    @Provides
    internal fun provideInspeccionFileDao(appDataBase: AppDataBase): InspeccionFileDao {
        return appDataBase.inspeccionFileDao()
    }
}