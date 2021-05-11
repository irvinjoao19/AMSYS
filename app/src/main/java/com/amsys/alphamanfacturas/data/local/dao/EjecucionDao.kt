package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.Ejecucion

@Dao
interface EjecucionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEjecucionTask(c: Ejecucion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEjecucionListTask(c: List<Ejecucion>)

    @Update
    fun updateEjecucionTask(vararg c: Ejecucion)

    @Delete
    fun deleteEjecucionTask(c: Ejecucion)

    @Query("SELECT * FROM Ejecucion WHERE inspeccionId =:id")
    fun getEjecucionTask(id:Int): Ejecucion

    @Query("DELETE FROM Ejecucion")
    fun deleteAll()

    @Query("SELECT * FROM Ejecucion WHERE inspeccionId =:id")
    fun getEjecucionById(id: Int): LiveData<Ejecucion>
}