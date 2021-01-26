package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.Contador

@Dao
interface ContadorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContadorTask(c: Contador)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContadorListTask(c: List<Contador>)

    @Update
    fun updateContadorTask(vararg c: Contador)

    @Delete
    fun deleteContadorTask(c: Contador)

    @Query("SELECT * FROM Contador WHERE inspeccionId=:id")
    fun getContadors(id:Int): LiveData<List<Contador>>

    @Query("SELECT * FROM Contador")
    fun getContadorTask(): Contador

    @Query("DELETE FROM Contador")
    fun deleteAll()

    @Query("SELECT * FROM Contador WHERE inspeccionId=:id")
    fun getContadoresTask(id: Int): List<Contador>
}