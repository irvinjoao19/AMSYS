package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.MecanismoFalla

@Dao
interface MecanismoFallaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMecanismoFallaTask(c: MecanismoFalla)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMecanismoFallaListTask(c: List<MecanismoFalla>)

    @Update
    fun updateMecanismoFallaTask(vararg c: MecanismoFalla)

    @Delete
    fun deleteMecanismoFallaTask(c: MecanismoFalla)

    @Query("SELECT * FROM MecanismoFalla")
    fun getMecanismoFalla(): LiveData<List<MecanismoFalla>>

    @Query("SELECT * FROM MecanismoFalla")
    fun getMecanismoFallaTask(): MecanismoFalla

    @Query("DELETE FROM MecanismoFalla")
    fun deleteAll()
}