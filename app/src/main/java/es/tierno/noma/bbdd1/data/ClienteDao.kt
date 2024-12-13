package es.tierno.noma.bbdd1.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ClienteDao {

    @Query ("SELECT id, nombre, apellidos, vip FROM cliente")
    suspend fun getAll(): List<ClienteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cliente: ClienteEntity)

    @Delete
    suspend fun delete(cliente: ClienteEntity)
}