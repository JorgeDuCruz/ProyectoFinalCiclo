package com.example.finalciclo.ViewModel.Repositories

import com.example.finalciclo.Model.DAO.JornadaDAO
import com.example.finalciclo.Model.Tablas.Jornada

class JornadaRepository(private val jornadaDAO: JornadaDAO) {
    suspend fun insertar(jornada: Jornada) {
        jornadaDAO.insertItem(jornada)
    }
}