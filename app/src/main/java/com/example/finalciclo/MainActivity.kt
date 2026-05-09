package com.example.finalciclo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalciclo.Interfaz.MainScreen
import com.example.finalciclo.ViewModel.MainViewModel
import com.example.finalciclo.ui.theme.FinalCicloTheme
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.History
import com.example.finalciclo.Interfaz.JornadasScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinalCicloTheme {
                // Estado para saber qué pantalla mostrar (0 = Registro, 1 = Historial, etc.)
                var selectedTabIndex by remember { mutableIntStateOf(0) }
                val viewModel: MainViewModel = viewModel()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = selectedTabIndex == 0,
                                onClick = { selectedTabIndex = 0 },
                                icon = { Icon(Icons.Default.EditCalendar, contentDescription = "Registro") },
                                label = { Text("Registro") }
                            )
                            NavigationBarItem(
                                selected = selectedTabIndex == 1,
                                onClick = { selectedTabIndex = 1 },
                                icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
                                label = { Text("Historial") }
                            )
                        }
                    }
                ) { innerPadding ->
                    // El contenedor que cambia según el índice seleccionado
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (selectedTabIndex) {
                            0 -> MainScreen(viewModel) // Tu pantalla de registro
                            1 -> JornadasScreen(viewModel)//Text("Aquí irá el Historial (Próximamente)")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinalCicloTheme {
        Greeting("Android")
    }
}