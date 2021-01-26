package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.PuntoMedida

@Dao
interface PuntoMedidaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPuntoMedidaTask(c: PuntoMedida)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPuntoMedidaListTask(c: List<PuntoMedida>)

    @Update
    fun updatePuntoMedidaTask(vararg c: PuntoMedida)

    @Delete
    fun deletePuntoMedidaTask(c: PuntoMedida)

    @Query("SELECT * FROM PuntoMedida WHERE inspeccionId=:id")
    fun getPuntoMedidas(id: Int): LiveData<List<PuntoMedida>>

    @Query("SELECT * FROM PuntoMedida")
    fun getPuntoMedidaTask(): PuntoMedida

    @Query("DELETE FROM PuntoMedida")
    fun deleteAll()

    @Query("SELECT * FROM PuntoMedida WHERE inspeccionId=:id")
    fun getPuntoMedidasTask(id: Int): List<PuntoMedida>
}