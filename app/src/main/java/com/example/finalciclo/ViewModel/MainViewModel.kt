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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

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
    val jornadasPorSemana: StateFlow<Map<Int, List<Jornada>>> = repository.allJornadas()
        .map { lista ->
            Log.d("BUSCAR","Jornada: ${lista}")
            lista.groupBy { jornada ->
                // Agrupamos por el número de semana del año
                Log.d("BUSCAR","Jornada: ${jornada}")
                jornada.fecha_entrada.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR)
            }
        }
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyMap())

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

        val (horas_normal, horas_especiales) = calcularDistribucionHoras(entrada,salida)

        // 2. Creamos el objeto de la entidad Jornada
        // Usamos 0 para el ID para que Room lo autogenere
        val nuevaJornada = Jornada(
            id = 0,
            fecha_entrada = entrada,
            fecha_salida = salida,
            hora_entrada = entrada.format(DateTimeFormatter.ofPattern("HH:mm")),
            hora_salida = salida.format(DateTimeFormatter.ofPattern("HH:mm")),
            horas_normales = horas_normal, // Aquí podrías poner el cálculo de duración
            horas_especiales = horas_especiales
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


    fun calcularDistribucionHoras(inicio: LocalDateTime, fin: LocalDateTime): Pair<Float, Float> {
        var horasNormales = 0f
        var horasEspeciales = 0f

        // Usamos un intervalo de 15 minutos para mayor precisión en el cálculo
        val pasoMinutos = 15L
        var tiempoActual = inicio

        while (tiempoActual.isBefore(fin)) {
            val proximoTramo = tiempoActual.plusMinutes(pasoMinutos)
            val finalDelTramo = if (proximoTramo.isAfter(fin)) fin else proximoTramo

            // Calculamos la duración de este tramo en horas (0.25f para 15 min)
            val duracionTramo = ChronoUnit.MINUTES.between(tiempoActual, finalDelTramo) / 60f

            if (esHoraEspecial(tiempoActual)) {
                horasEspeciales += duracionTramo
            } else {
                horasNormales += duracionTramo
            }

            tiempoActual = proximoTramo
        }

        return Pair(horasNormales, horasEspeciales)
    }

    private fun esHoraEspecial(momento: LocalDateTime): Boolean {
        // 1. Comprobación de Fin de Semana (Sábado o Domingo)
        val esFinde = momento.dayOfWeek == DayOfWeek.SATURDAY || momento.dayOfWeek == DayOfWeek.SUNDAY

        // 2. Comprobación de Horario Nocturno (22:00 a 06:00)
        val hora = momento.hour
        val esNocturno = hora >= 22 || hora < 6

        // 3. Comprobación de Festivos (Aquí deberías añadir tu lista de festivos)
        val esFestivo = consultarCalendarioFestivos(momento)

        return esFinde || esNocturno || esFestivo
    }

    private fun consultarCalendarioFestivos(fecha: LocalDateTime): Boolean {
        // Implementación sencilla para ejemplo.
        // En el futuro podrías leer esto de una tabla de "Festivos" en Room.
        val diaMes = "${fecha.dayOfMonth}/${fecha.monthValue}"
        val festivosFijos = listOf("1/1", "6/1", "1/5", "15/8", "12/10", "1/11", "6/12", "8/12", "25/12")
        return festivosFijos.contains(diaMes)
    }
}
