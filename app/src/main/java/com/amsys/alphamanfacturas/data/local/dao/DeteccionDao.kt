package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.Deteccion

@Dao
interface DeteccionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDeteccionTask(c: Deteccion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDeteccionListTask(c: List<Deteccion>)

    @Update
    fun updateDeteccionTask(vararg c: Deteccion)

    @Delete
    fun deleteDeteccionTask(c: Deteccion)

    @Query("SELECT * FROM Deteccion")
    fun getDeteccion(): LiveData<List<Deteccion>>

    @Query("SELECT * FROM Deteccion")
    fun getDeteccionTask(): Deteccion

    @Query("DELETE FROM Deteccion")
    fun deleteAll()
}