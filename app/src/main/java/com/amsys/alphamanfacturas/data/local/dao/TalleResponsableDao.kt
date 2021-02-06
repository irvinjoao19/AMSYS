package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.TalleResponsable

@Dao
interface TalleResponsableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTalleResponsableTask(c: TalleResponsable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTalleResponsableListTask(c: List<TalleResponsable>)

    @Update
    fun updateTalleResponsableTask(vararg c: TalleResponsable)

    @Delete
    fun deleteTalleResponsableTask(c: TalleResponsable)

    @Query("SELECT * FROM TalleResponsable")
    fun getTalleResponsable(): LiveData<List<TalleResponsable>>

    @Query("SELECT * FROM TalleResponsable")
    fun getTalleResponsableTask(): TalleResponsable

    @Query("DELETE FROM TalleResponsable")
    fun deleteAll()
}