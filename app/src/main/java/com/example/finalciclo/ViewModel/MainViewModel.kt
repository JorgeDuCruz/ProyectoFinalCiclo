package com.example.finalciclo.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

class MainViewModel : ViewModel() {
    // Inicialización con fecha y hora actual según el requisito [cite: 17]
    private val _fechaEntrada = MutableStateFlow(LocalDateTime.now())
    val fechaEntrada: StateFlow<LocalDateTime> = _fechaEntrada

    private val _fechaSalida = MutableStateFlow(LocalDateTime.now())
    val fechaSalida: StateFlow<LocalDateTime> = _fechaSalida

    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    fun updateFechaEntrada(nuevaFecha: LocalDateTime) {
        _fechaEntrada.value = nuevaFecha
    }

    fun updateFechaSalida(nuevaFecha: LocalDateTime) {
        _fechaSalida.value = nuevaFecha
    }

    fun guardarFechas() {
        val entradaStr = _fechaEntrada.value.format(formatter)
        val salidaStr = _fechaSalida.value.format(formatter)
        // Log de guardado solicitado
        println("guardado $entradaStr hasta $salidaStr")
    }

    fun updateHoraEntrada(hora: Int, minuto: Int) {
        val actual = _fechaEntrada.value
        _fechaEntrada.value = actual.withHour(hora).withMinute(minuto)
    }

    fun updateHoraSalida(hora: Int, minuto: Int) {
        val actual = _fechaSalida.value
        _fechaSalida.value = actual.withHour(hora).withMinute(minuto)
    }
}
