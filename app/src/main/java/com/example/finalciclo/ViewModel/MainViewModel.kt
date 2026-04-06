package com.example.finalciclo.ViewModel

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
    fun updateHoraDesdeTexto(esEntrada: Boolean, texto: String) {
        try {
            val partes = texto.split(":")
            if (partes.size == 2) {
                val h = partes[0].toInt().coerceIn(0, 23)
                val m = partes[1].toInt().coerceIn(0, 59)

                if (esEntrada) {
                    _fechaEntrada.value = _fechaEntrada.value.withHour(h).withMinute(m)
                } else {
                    _fechaSalida.value = _fechaSalida.value.withHour(h).withMinute(m)
                }
            }
        } catch (e: Exception) {
            // Manejo de error silencioso para evitar crashes durante el tipeo
        }
    }
}
