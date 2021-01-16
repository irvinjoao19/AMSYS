package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.Consecuencia

@Dao
interface ConsecuenciaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConsecuenciaTask(c: Consecuencia)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConsecuenciaListTask(c: List<Consecuencia>)

    @Update
    fun updateConsecuenciaTask(vararg c: Consecuencia)

    @Delete
    fun deleteConsecuenciaTask(c: Consecuencia)

    @Query("SELECT * FROM Consecuencia")
    fun getConsecuencia(): LiveData<List<Consecuencia>>

    @Query("SELECT * FROM Consecuencia")
    fun getConsecuenciaTask(): Consecuencia

    @Query("DELETE FROM Consecuencia")
    fun deleteAll()
}