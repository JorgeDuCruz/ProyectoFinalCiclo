package com.example.finalciclo.Model.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.finalciclo.Model.Tablas.Jornada

@Dao
interface JornadaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(jornada : Jornada)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleItems(jornadas: List<Jornada>)
}