package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.InspeccionFile

@Dao
interface InspeccionFileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInspeccionFileTask(c: InspeccionFile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInspeccionFileListTask(c: List<InspeccionFile>)

    @Update
    fun updateInspeccionFileTask(vararg c: InspeccionFile)

    @Delete
    fun deleteInspeccionFileTask(c: InspeccionFile)

    @Query("SELECT * FROM InspeccionFile WHERE inspeccionId=:id")
    fun getInspeccionFiles(id: Int): LiveData<List<InspeccionFile>>

    @Query("SELECT * FROM InspeccionFile")
    fun getInspeccionFileTask(): InspeccionFile

    @Query("DELETE FROM InspeccionFile")
    fun deleteAll()

    @Query("SELECT * FROM InspeccionFile WHERE inspeccionId=:id")
    fun getInspeccionFilesTask(id: Int): List<InspeccionFile>
}