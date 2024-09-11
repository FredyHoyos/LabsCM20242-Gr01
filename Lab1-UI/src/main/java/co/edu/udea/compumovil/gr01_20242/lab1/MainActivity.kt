package co.edu.udea.compumovil.gr01_20242.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import co.edu.udea.compumovil.gr01_20242.lab1.ui.theme.Labs20242Gr01Theme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labs20242Gr01Theme {
                Titulo()
                Cuerpo()
            }
        }
    }
}

@Composable
fun Titulo(modifier: Modifier = Modifier) {
    Text(
        text = "Información Personal",
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

@Composable
fun Cuerpo(modifier: Modifier = Modifier) {

    Column (
        modifier = Modifier.padding(10.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row{
            Image(painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Imagen nombre",
                modifier = Modifier.size(70.dp).padding(vertical = 8.dp))
            EntradaDeTexto("nombre")
        }
        Row{
            Image(painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "Imagen apellido",
                modifier = Modifier.size(70.dp).padding(vertical = 8.dp))
            EntradaDeTexto("apellido")

        }
        Row{
            Image(painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "Imagen sexo",
                modifier = Modifier.size(70.dp).padding(vertical = 8.dp))
            Text("Sexo: ")

        }
        Row{
            Image(painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "Imagen sexo",
                modifier = Modifier.size(70.dp).padding(vertical = 8.dp))
            Text("Fecha de nacimiento: ")
            DatePickerDocked()

        }
    }
}


@Composable
fun EntradaDeTexto(visual: String) {
    val texto = remember { mutableStateOf("") }

    TextField(
        value = texto.value,
        onValueChange = { newText ->
            texto.value = newText
        },
        label = { Text("Escriba su $visual") },
        modifier = Modifier.padding(vertical = 8.dp)
    )
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Labs20242Gr01Theme {
        Column {
            Titulo()
            Cuerpo()
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(modifier: Modifier = Modifier) {
    val showDatePicker: MutableState<Boolean> = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Select Date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker.value = !showDatePicker.value }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker.value) {
            Popup(
                onDismissRequest = { showDatePicker.value = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }

    // Observa cambios en la selección de fecha
    LaunchedEffect(datePickerState.selectedDateMillis) {
        if (datePickerState.selectedDateMillis != null) {
            showDatePicker.value = false
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}