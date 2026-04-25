package com.example.finalciclo.Interfaz

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.finalciclo.ViewModel.MainViewModel
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.Composable
import java.time.LocalDateTime
import java.util.Locale

@Composable
fun MainScreen(viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    // Recolección de estados del ViewModel
    val fechaEntrada by viewModel.fechaEntrada.collectAsState()
    val fechaSalida by viewModel.fechaSalida.collectAsState()
    val context = LocalContext.current

    // Delegamos la representación visual a MainContent
    MainContent(
        fechaEntrada = fechaEntrada,
        fechaSalida = fechaSalida,
        onEntradaClick = {
            showDatePicker(context, fechaEntrada) { viewModel.updateFechaEntrada(it) }
        },
        onSalidaClick = {
            showDatePicker(context, fechaSalida) { viewModel.updateFechaSalida(it) }
        },
        onHoraEntradaClick = {
            showTimePicker(context, fechaEntrada) { h, m -> viewModel.updateHoraEntrada(h, m) }
        },
        onMinutoEntradaClick = {
            showTimePicker(context, fechaEntrada) { h, m -> viewModel.updateHoraEntrada(h, m) }
        },
        // Nuevas funciones para Salida
        onHoraSalidaClick = {
            showTimePicker(context, fechaSalida) { h, m -> viewModel.updateHoraSalida(h, m) }
        },
        onMinutoSalidaClick = {
            showTimePicker(context, fechaSalida) { h, m -> viewModel.updateHoraSalida(h, m) }
        },
        onGuardarClick = { viewModel.guardarFechas() }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    fechaEntrada: LocalDateTime,
    fechaSalida: LocalDateTime,
    onEntradaClick: () -> Unit,
    onSalidaClick: () -> Unit,
    onHoraEntradaClick: () -> Unit,
    onMinutoEntradaClick: () -> Unit,
    onHoraSalidaClick: () -> Unit,
    onMinutoSalidaClick: () -> Unit,
    onGuardarClick: () -> Unit
) {
    val dateDeviceInfo = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Registro de Fechas", style = MaterialTheme.typography.headlineSmall)

        // Sección Entrada
        DateTimeSelectorGroup(
            label = "Entrada",
            fechaTexto = fechaEntrada.format(dateDeviceInfo),
            horaValor = String.format(Locale.getDefault(),"%02d:%02d", fechaEntrada.hour, fechaEntrada.minute),
            onDateClick = onEntradaClick,
            onHorasClick = onHoraEntradaClick,
            onMinutosClick = onMinutoEntradaClick
        )

        // Sección Salida
        DateTimeSelectorGroup(
            label = "Salida",
            fechaTexto = fechaSalida.format(dateDeviceInfo),
            horaValor = String.format(Locale.getDefault(),"%02d:%02d", fechaSalida.hour, fechaSalida.minute),
            onDateClick = onSalidaClick,
            onHorasClick = onHoraSalidaClick,
            onMinutosClick = onMinutoSalidaClick
        )

        Button(onClick = onGuardarClick, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar")
        }
    }
}


// Función auxiliar para mostrar el DatePickerDialog clásico de Android
fun showDatePicker(context: android.content.Context, currentDate: LocalDateTime, onDateSelected: (LocalDateTime) -> Unit) {
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Opción A: Mantener la hora que ya tenía el objeto original
            val nuevaFechaHora = LocalDateTime.of(
                year,
                month + 1,
                dayOfMonth,
                currentDate.hour,
                currentDate.minute
            )

            onDateSelected(nuevaFechaHora)
        },
        currentDate.year,
        currentDate.monthValue - 1, // Forzado a hacerlo así por que DatePickerDialog funciona del 0-11 pero el LocalDateTime es 1-12
        currentDate.dayOfMonth
    ).show()
}

fun showTimePicker(
    context: android.content.Context,
    fechaActual: LocalDateTime,
    onTimeSelected: (Int, Int) -> Unit
) {
    TimePickerDialog(
        context,
        { _, hour, minute ->
            onTimeSelected(hour, minute)
        },
        fechaActual.hour,
        fechaActual.minute,
        true // true para formato 24h
    ).show()
}
@Composable
fun DateTimeSelectorGroup(
    label: String,
    fechaTexto: String,
    horaValor: String, // Asumimos formato "HH:mm"
    onDateClick: () -> Unit,
    onHorasClick: () -> Unit,   // <--- Nueva
    onMinutosClick: () -> Unit  // <--- Nueva
) {
    // Extraemos horas y minutos de la variable existente
    val partes = horaValor.split(":")
    val horas = partes.getOrNull(0) ?: ""
    val minutos = partes.getOrNull(1) ?: ""

    Column {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 1. "Desplegable" de Fecha
            OutlinedCard(
                onClick = onDateClick,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(fechaTexto)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }

            // 2. Selectores divididos para Horas y Minutos
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Selector de Horas
                OutlinedCard(
                    onClick = onHorasClick, // Acción para abrir el selector de horas
                    modifier = Modifier.width(65.dp)
                ) {
                    Box(modifier = Modifier.padding(12.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        // Si horas está vacío, mostramos "HH" como ayuda
                        Text(text = horas.ifEmpty { "HH" }, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Text(":", style = MaterialTheme.typography.bodyLarge)

                // Selector de Minutos
                OutlinedCard(
                    onClick = onMinutosClick, // Acción para abrir el selector de minutos
                    modifier = Modifier.width(65.dp)
                ) {
                    Box(modifier = Modifier.padding(12.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        // Si minutos está vacío, mostramos "mm"
                        Text(text = minutos.ifEmpty { "mm" }, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}