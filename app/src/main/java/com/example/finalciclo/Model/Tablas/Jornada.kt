package com.example.finalciclo.Model.Tablas

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity (tableName = "Jornadas")
data class Jornada (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "fecha_entrada") val fecha_entrada: LocalDateTime,
    @ColumnInfo(name = "fecha_salida") val fecha_salida: LocalDateTime,
    @ColumnInfo(name = "hora_entrada") val hora_entrada: String,
    @ColumnInfo(name = "hora_salida") val hora_salida: String,
    @ColumnInfo(name = "horas_normales") val horas_normales: Float,
    @ColumnInfo(name = "horas_especiales") val horas_especiales: Float,
)