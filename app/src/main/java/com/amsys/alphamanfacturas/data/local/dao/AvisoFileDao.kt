package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.AvisoFile

@Dao
interface AvisoFileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAvisoFileTask(c: AvisoFile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAvisoFileListTask(c: List<AvisoFile>)

    @Update
    fun updateAvisoFileTask(vararg c: AvisoFile)

    @Delete
    fun deleteAvisoFileTask(c: AvisoFile)

    @Query("SELECT * FROM AvisoFile WHERE avisoId=:id")
    fun getAvisoFiles(id: Int): LiveData<List<AvisoFile>>

    @Query("SELECT * FROM AvisoFile")
    fun getAvisoFileTask(): AvisoFile

    @Query("DELETE FROM AvisoFile")
    fun deleteAll()

    @Query("SELECT * FROM AvisoFile WHERE avisoId=:id AND estado = 0")
    fun getAvisoFilesTask(id: Int): List<AvisoFile>

    @Query("UPDATE AvisoFile SET estado = 1 WHERE avisoId=:id")
    fun updateEnableAvisoFile(id: Int)
}