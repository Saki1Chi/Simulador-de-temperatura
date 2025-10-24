package com.ejemplo.simuladortemperatura

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.simuladortemperatura.ui.theme.MiSimuladorTemperaturaTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiSimuladorTemperaturaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SimuladorTemperaturaScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SimuladorTemperaturaScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val temperatureManager = remember { TemperatureManager() }

    var temperature by remember { mutableStateOf(temperatureManager.generateRandomTemperature()) }
    var temperatureColor by remember { mutableStateOf(temperatureManager.getTemperatureColor(temperature)) }
    var status by remember { mutableStateOf(temperatureManager.getTemperatureStatus(temperature)) }

    fun updateTemperature(newTemp: Double) {
        temperature = newTemp
        temperatureColor = temperatureManager.getTemperatureColor(newTemp)
        status = temperatureManager.getTemperatureStatus(newTemp)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Simulador de Temperatura",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Temperatura Actual",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = String.format("%.1f°C", temperature),
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = temperatureColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = status,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = temperatureColor
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val newTemp = temperatureManager.generateRandomTemperature()
                updateTemperature(newTemp)
                Toast.makeText(context, "Nueva temperatura generada!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Generar Nueva Temperatura",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                val newTemp = temperatureManager.generateRandomTemperature()
                updateTemperature(newTemp)
                Toast.makeText(context, "Temperatura reseteada", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Resetear",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// Clase TemperatureManager (en el mismo archivo para simplificar)
class TemperatureManager {

    fun generateRandomTemperature(): Double {
        // 20.0 a 50.9 °C
        return (Random.nextInt(200, 510) / 10.0)
    }

    fun getTemperatureColor(temperature: Double): Color {
        return when {
            temperature < 25.0 -> Color(0xFF2196F3) // Azul (Frío)
            temperature < 35.0 -> Color(0xFF4CAF50) // Verde (Normal)
            else -> Color(0xFFF44336)               // Rojo (Caliente)
        }
    }

    fun getTemperatureStatus(temperature: Double): String {
        return when {
            temperature < 25.0 -> "🌡️ Frío"
            temperature < 35.0 -> "✅ Normal"
            else -> "🔥 Caliente"
        }
    }
}
