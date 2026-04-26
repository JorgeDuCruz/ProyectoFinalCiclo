package com.example.finalciclo.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalciclo.Model.BD.AppDatabase
import com.example.finalciclo.Model.Tablas.Jornada
import com.example.finalciclo.ViewModel.Repositories.JornadaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

class MainViewModel(application: Application) : AndroidViewModel(application) {
    // Inicialización con fecha y hora actual según el requisito [cite: 17]
    private val _fechaEntrada = MutableStateFlow(LocalDateTime.now())
    val fechaEntrada: StateFlow<LocalDateTime> = _fechaEntrada

    private val _fechaSalida = MutableStateFlow(LocalDateTime.now())
    val fechaSalida: StateFlow<LocalDateTime> = _fechaSalida

    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    private val repository: JornadaRepository

    init {
        // Usas 'application' para obtener el contexto de forma segura
        val dao = AppDatabase.getDatabase(application).jornadaDAO()
        repository = JornadaRepository(dao)
    }
    fun updateFechaEntrada(nuevaFecha: LocalDateTime) {
        _fechaEntrada.value = nuevaFecha
    }

    fun updateFechaSalida(nuevaFecha: LocalDateTime) {
        _fechaSalida.value = nuevaFecha
    }

    fun guardarFechas() {
        // 1. Obtenemos los valores actuales de los StateFlow
        val entrada = _fechaEntrada.value
        val salida = _fechaSalida.value

        // 2. Creamos el objeto de la entidad Jornada
        // Usamos 0 para el ID para que Room lo autogenere
        val nuevaJornada = Jornada(
            id = 0,
            fecha_entrada = entrada,
            fecha_salida = salida,
            hora_entrada = entrada.format(DateTimeFormatter.ofPattern("HH:mm")),
            hora_salida = salida.format(DateTimeFormatter.ofPattern("HH:mm")),
            horas_normales = 8.0f, // Aquí podrías poner el cálculo de duración
            horas_especiales = 0.0f
        )

        // 3. Lanzamos una corrutina en el hilo de E/S (IO)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertar(nuevaJornada)

                // Log de confirmación
                val entradaStr = entrada.format(formatter)
                val salidaStr = salida.format(formatter)
                Log.d("GUARDAR", "Éxito: Guardado $entradaStr hasta $salidaStr en SQLite")

            } catch (e: Exception) {
                Log.e("GUARDAR", "Error al guardar en la base de datos: ${e.message}")
            }
        }
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
