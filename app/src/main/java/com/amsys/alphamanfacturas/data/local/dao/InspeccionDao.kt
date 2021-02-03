package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.Inspeccion

@Dao
interface InspeccionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInspeccionTask(c: Inspeccion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInspeccionListTask(c: List<Inspeccion>)

    @Update
    fun updateInspeccionTask(vararg c: Inspeccion)

    @Delete
    fun deleteInspeccionTask(c: Inspeccion)

    @Query("SELECT * FROM Inspeccion ORDER BY inspeccionId DESC ")
    fun getInspeccions(): LiveData<List<Inspeccion>>

    @Query("SELECT * FROM Inspeccion")
    fun getInspeccionTask(): Inspeccion

    @Query("DELETE FROM Inspeccion")
    fun deleteAll()
}