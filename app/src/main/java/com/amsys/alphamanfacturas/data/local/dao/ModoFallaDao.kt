package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.ModoFalla

@Dao
interface ModoFallaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModoFallaTask(c: ModoFalla)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModoFallaListTask(c: List<ModoFalla>)

    @Update
    fun updateModoFallaTask(vararg c: ModoFalla)

    @Delete
    fun deleteModoFallaTask(c: ModoFalla)

    @Query("SELECT * FROM ModoFalla")
    fun getModoFallas(): LiveData<List<ModoFalla>>

    @Query("SELECT * FROM ModoFalla")
    fun getModoFallaTask(): ModoFalla

    @Query("DELETE FROM ModoFalla")
    fun deleteAll()
}