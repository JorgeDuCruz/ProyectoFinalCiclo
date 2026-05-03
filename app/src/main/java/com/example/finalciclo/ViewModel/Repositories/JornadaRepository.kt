package com.example.finalciclo.ViewModel.Repositories

import android.util.Log
import com.example.finalciclo.Model.DAO.JornadaDAO
import com.example.finalciclo.Model.Tablas.Jornada
import kotlinx.coroutines.flow.Flow

class JornadaRepository(private val jornadaDAO: JornadaDAO) {
    fun allJornadas(): Flow<List<Jornada>>{
        Log.d("BUSCAR","Pido jornadas")
        val jornadas = jornadaDAO.getAllJornadas()
        Log.d("BUSCAR","jordanas: ${jornadas}")
        return jornadas
    }

    suspend fun insertar(jornada: Jornada) {
        jornadaDAO.insertItem(jornada)
    }

}