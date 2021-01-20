package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.Equipo

@Dao
interface EquipoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEquipoTask(c: Equipo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEquipoListTask(c: List<Equipo>)

    @Update
    fun updateEquipoTask(vararg c: Equipo)

    @Delete
    fun deleteEquipoTask(c: Equipo)

    @Query("SELECT * FROM Equipo")
    fun getEquipos(): LiveData<List<Equipo>>

    @Query("SELECT * FROM Equipo")
    fun getEquipoTask(): Equipo

    @Query("DELETE FROM Equipo")
    fun deleteAll()
}