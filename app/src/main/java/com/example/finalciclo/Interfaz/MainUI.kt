package com.example.finalciclo.Interfaz

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
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
        onHoraEntradaChange = { nuevoTexto ->
            viewModel.updateHoraDesdeTexto(esEntrada = true, texto = nuevoTexto)
        },
        onHoraSalidaChange = { nuevoTexto ->
            viewModel.updateHoraDesdeTexto(esEntrada = false, texto = nuevoTexto)
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
    onHoraEntradaChange: (String) -> Unit,
    onHoraSalidaChange: (String) -> Unit,
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
            onHoraChange = onHoraEntradaChange
        )

        // Sección Salida
        DateTimeSelectorGroup(
            label = "Salida",
            fechaTexto = fechaSalida.format(dateDeviceInfo),
            horaValor = String.format(Locale.getDefault(),"%02d:%02d", fechaSalida.hour, fechaSalida.minute),
            onDateClick = onSalidaClick,
            onHoraChange = onHoraSalidaChange
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

            // Opción B: Establecer a las 00:00 (inicio del día)
            // val nuevaFechaHora = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0)

            onDateSelected(nuevaFechaHora)
        },
        currentDate.year,
        currentDate.monthValue - 1,
        currentDate.dayOfMonth
    ).show()
}

@Composable
fun DateTimeSelectorGroup(
    label: String,
    fechaTexto: String,
    horaValor: String,
    onDateClick: () -> Unit,
    onHoraChange: (String) -> Unit
) {
    Column {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 1. "Desplegable" de Fecha (Día/Mes/Año)
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

            // 2. Campo para rellenar Hora y Minutos
            OutlinedTextField(
                value = horaValor,
                onValueChange = onHoraChange,
                modifier = Modifier.width(100.dp),
                label = { Text("HH:mm") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
    }
}



@Preview(showBackground = true, name = "Vista de Registro")
@Composable
fun MainScreenPreview() {
    // Datos simulados para la previsualización [cite: 20]
    val fechaSimuladaEntrada = LocalDateTime.of(2026, 4, 6, 9, 30)
    val fechaSimuladaSalida = LocalDateTime.of(2026, 4, 6, 18, 15)

    MaterialTheme {
        MainContent(
            fechaEntrada = fechaSimuladaEntrada,
            fechaSalida = fechaSimuladaSalida,
            onEntradaClick = {},
            onSalidaClick = {},
            // Simulación de los callbacks de cambio de hora [cite: 55]
            onHoraEntradaChange = {},
            onHoraSalidaChange = {},
            onGuardarClick = {}
        )
    }
}

