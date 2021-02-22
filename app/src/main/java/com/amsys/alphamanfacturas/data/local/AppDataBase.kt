package com.amsys.alphamanfacturas.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amsys.alphamanfacturas.data.local.dao.*
import com.amsys.alphamanfacturas.data.local.model.*

@Database(
    entities = [
        Usuario::class,
        Aviso::class,
        Consecuencia::class,
        Prioridad::class,
        Parada::class,
        Deteccion::class,
        MecanismoFalla::class,
        Impacto::class,
        CausaFalla::class,
        Registro::class,
        AvisoFile::class,
        Equipo::class,
        ModoFalla::class,
        TipoParada::class,
        SubTipoParada::class,
        Inspeccion::class,
        PuntoMedida::class,
        Aspecto::class,
        Contador::class,
        InspeccionFile::class,
        TalleResponsable::class
    ],
    version = 4, // version 1 en play store
    exportSchema = false
)
@TypeConverters(Converts::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun avisoDao(): AvisoDao
    abstract fun consecuenciaDao(): ConsecuenciaDao
    abstract fun prioridadDao(): PrioridadDao
    abstract fun paradaDao(): ParadaDao
    abstract fun deteccionDao(): DeteccionDao
    abstract fun mecanismoFallaDao(): MecanismoFallaDao
    abstract fun impactoDao(): ImpactoDao
    abstract fun causaFallaDao(): CausaFallaDao
    abstract fun registroDao(): RegistroDao
    abstract fun avisoFileDao(): AvisoFileDao
    abstract fun equipoDao(): EquipoDao
    abstract fun modoFallaDao(): ModoFallaDao
    abstract fun tipoParadaDao(): TipoParadaDao
    abstract fun subTipoParadaDao(): SubTipoParadaDao

    abstract fun inspeccionDao(): InspeccionDao
    abstract fun puntoMedidaDao(): PuntoMedidaDao
    abstract fun aspectoDao(): AspectoDao
    abstract fun contadorDao(): ContadorDao
    abstract fun inspeccionFileDao(): InspeccionFileDao
    abstract fun talleResponsableDao(): TalleResponsableDao

    companion object {
        @Volatile
        var INSTANCE: AppDataBase? = null
        val DB_NAME = "amsys_db"
    }

    fun getDatabase(context: Context): AppDataBase {
        if (INSTANCE == null) {
            synchronized(AppDataBase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java, "amsys_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }
        return INSTANCE!!
    }
}