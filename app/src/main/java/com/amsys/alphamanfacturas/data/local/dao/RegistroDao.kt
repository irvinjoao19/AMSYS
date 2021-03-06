package com.amsys.alphamanfacturas.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amsys.alphamanfacturas.data.local.model.Converts
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
    fun getRegistroExistByIdTask(id: Int): Registro?

    @Query("SELECT * FROM Registro WHERE registroId=:id")
    fun getRegistroById(id: Int): LiveData<Registro>


    @Query("SELECT registroId FROM Registro ORDER BY registroId DESC LIMIT 1")
    fun getIdentity(): LiveData<Int>

    @TypeConverters(Converts::class)
    @Query("UPDATE Registro SET adjuntos =:i WHERE registroId=:id")
    fun updateRegistroAdjuntos(i: String, id: Int)
}