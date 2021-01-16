package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.CausaFalla

@Dao
interface CausaFallaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCausaFallaTask(c: CausaFalla)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCausaFallaListTask(c: List<CausaFalla>)

    @Update
    fun updateCausaFallaTask(vararg c: CausaFalla)

    @Delete
    fun deleteCausaFallaTask(c: CausaFalla)

    @Query("SELECT * FROM CausaFalla")
    fun getCausaFalla(): LiveData<List<CausaFalla>>

    @Query("SELECT * FROM CausaFalla")
    fun getCausaFallaTask(): CausaFalla

    @Query("DELETE FROM CausaFalla")
    fun deleteAll()
}