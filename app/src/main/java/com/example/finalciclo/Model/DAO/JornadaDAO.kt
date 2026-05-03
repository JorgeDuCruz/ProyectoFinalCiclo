package com.example.finalciclo.Model.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.finalciclo.Model.Tablas.Jornada
import kotlinx.coroutines.flow.Flow

@Dao
interface JornadaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(jornada : Jornada)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleItems(jornadas: List<Jornada>)

    @Query("SELECT * FROM Jornadas ORDER BY fecha_entrada DESC")
    fun getAllJornadas(): Flow<List<Jornada>>
}