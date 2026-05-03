package com.example.finalciclo.Model.TypeConverters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JornadaConverter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let {
            // Convertimos el String primero a LocalDate (que solo es fecha)
            val date = LocalDate.parse(it, formatter)
            // Le añadimos la hora 00:00:00 para convertirlo en LocalDateTime
            date.atStartOfDay()
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
}