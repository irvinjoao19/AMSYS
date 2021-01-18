package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.Registro

@Dao
interface RegistroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroTask(c: Registro)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroListTask(c: List<Registro>)

    @Update
    fun updateRegistroTask(vararg c: Registro)

    @Delete
    fun deleteRegistroTask(c: Registro)

    @Query("SELECT * FROM Registro")
    fun getRegistros(): LiveData<List<Registro>>

    @Query("SELECT * FROM Registro")
    fun getRegistroTask(): Registro

    @Query("DELETE FROM Registro")
    fun deleteAll()

    @Query("SELECT * FROM Registro WHERE registroId=:id")
    fun getRegistroByIdTask(id: Int): Registro

    @Query("SELECT * FROM Registro WHERE registroId=:id")
    fun getRegistroById(id: Int): LiveData<Registro>
}