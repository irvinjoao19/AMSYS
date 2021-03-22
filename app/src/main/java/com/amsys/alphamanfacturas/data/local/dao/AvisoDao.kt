package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.Aviso

@Dao
interface AvisoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAvisoTask(c: Aviso)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAvisoListTask(c: List<Aviso>)

    @Update
    fun updateAvisoTask(vararg c: Aviso)

    @Delete
    fun deleteAvisoTask(c: Aviso)

    @Query("SELECT * FROM Aviso ORDER BY avisoId DESC")
    fun getAvisos(): LiveData<List<Aviso>>

    @Query("SELECT * FROM Aviso")
    fun getAvisoTask(): Aviso

    @Query("DELETE FROM Aviso")
    fun deleteAll()

    @Query("UPDATE Aviso SET finParada=:f, editable =:e WHERE avisoId=:id")
    fun updateFechaFinParadaAviso(id: Int, f: String, e: Boolean)
}