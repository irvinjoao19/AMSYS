package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.Impacto

@Dao
interface ImpactoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImpactoTask(c: Impacto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImpactoListTask(c: List<Impacto>)

    @Update
    fun updateImpactoTask(vararg c: Impacto)

    @Delete
    fun deleteImpactoTask(c: Impacto)

    @Query("SELECT * FROM Impacto")
    fun getImpacto(): LiveData<List<Impacto>>

    @Query("SELECT * FROM Impacto")
    fun getImpactoTask(): Impacto

    @Query("DELETE FROM Impacto")
    fun deleteAll()
}