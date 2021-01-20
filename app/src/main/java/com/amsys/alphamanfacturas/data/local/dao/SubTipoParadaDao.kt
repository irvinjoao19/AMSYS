package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.SubTipoParada

@Dao
interface SubTipoParadaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubTipoParadaTask(c: SubTipoParada)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubTipoParadaListTask(c: List<SubTipoParada>)

    @Update
    fun updateSubTipoParadaTask(vararg c: SubTipoParada)

    @Delete
    fun deleteSubTipoParadaTask(c: SubTipoParada)

    @Query("SELECT * FROM SubTipoParada")
    fun getSubTipoParadas(): LiveData<List<SubTipoParada>>

    @Query("SELECT * FROM SubTipoParada")
    fun getSubTipoParadaTask(): SubTipoParada

    @Query("DELETE FROM SubTipoParada")
    fun deleteAll()
}