package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.Parada

@Dao
interface ParadaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParadaTask(c: Parada)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParadaListTask(c: List<Parada>)

    @Update
    fun updateParadaTask(vararg c: Parada)

    @Delete
    fun deleteParadaTask(c: Parada)

    @Query("SELECT * FROM Parada")
    fun getParada(): LiveData<Parada>

    @Query("SELECT * FROM Parada")
    fun getParadaTask(): Parada

    @Query("DELETE FROM Parada")
    fun deleteAll()
}