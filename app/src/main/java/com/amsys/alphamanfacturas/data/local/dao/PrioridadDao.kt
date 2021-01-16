package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.Prioridad

@Dao
interface PrioridadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrioridadTask(c: Prioridad)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrioridadListTask(c: List<Prioridad>)

    @Update
    fun updatePrioridadTask(vararg c: Prioridad)

    @Delete
    fun deletePrioridadTask(c: Prioridad)

    @Query("SELECT * FROM Prioridad")
    fun getPrioridad(): LiveData<List<Prioridad>>

    @Query("SELECT * FROM Prioridad")
    fun getPrioridadTask(): Prioridad

    @Query("DELETE FROM Prioridad")
    fun deleteAll()
}