package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.TipoParada

@Dao
interface TipoParadaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTipoParadaTask(c: TipoParada)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTipoParadaListTask(c: List<TipoParada>)

    @Update
    fun updateTipoParadaTask(vararg c: TipoParada)

    @Delete
    fun deleteTipoParadaTask(c: TipoParada)

    @Query("SELECT * FROM TipoParada")
    fun getTipoParadas(): LiveData<List<TipoParada>>

    @Query("SELECT * FROM TipoParada")
    fun getTipoParadaTask(): TipoParada

    @Query("DELETE FROM TipoParada")
    fun deleteAll()
}