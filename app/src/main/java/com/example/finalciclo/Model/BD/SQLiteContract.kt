package com.example.finalciclo.Model.BD

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.finalciclo.Model.DAO.JornadaDAO
import com.example.finalciclo.Model.Tablas.Jornada
import com.example.finalciclo.Model.TypeConverters.JornadaConverter

@Database(entities = [Jornada::class], version = 1)
@TypeConverters(JornadaConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jornadaDAO(): JornadaDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ControlLaboral"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}