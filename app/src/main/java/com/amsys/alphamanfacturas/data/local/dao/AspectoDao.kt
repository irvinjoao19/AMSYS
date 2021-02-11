package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.Aspecto

@Dao
interface AspectoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAspectoTask(c: Aspecto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAspectoListTask(c: List<Aspecto>)

    @Update
    fun updateAspectoTask(vararg c: Aspecto)

    @Delete
    fun deleteAspectoTask(c: Aspecto)

    @Query("SELECT * FROM Aspecto WHERE inspeccionId=:id ORDER BY orden,nroMuestra ASC")
    fun getAspectos(id: Int): LiveData<List<Aspecto>>

    @Query("SELECT * FROM Aspecto")
    fun getAspectoTask(): Aspecto

    @Query("DELETE FROM Aspecto")
    fun deleteAll()

    @Query("SELECT * FROM Aspecto WHERE inspeccionId=:id AND editable =:b")
    fun getAspectosTask(id: Int,b:Boolean): List<Aspecto>
}