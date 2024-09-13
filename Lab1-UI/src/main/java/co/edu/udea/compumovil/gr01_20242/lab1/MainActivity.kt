package co.edu.udea.compumovil.gr01_20242.lab1

import android.os.Bundle
import android.text.style.BackgroundColorSpan
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Titulo() {
    TopAppBar(
        title = { Text("Información Personal") },
        Modifier.background(Color.Red, RectangleShape)
    )

}



@Composable
fun Cuerpo() {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }


    Column (
        modifier = Modifier.padding(10.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), // que la fila ocupe todo el ancho disponible
            verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente en el centro
        ){
            Image(painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Imagen nombre",
                modifier = Modifier.size(70.dp).padding(vertical = 8.dp))
            OutlinedTextField(
                value = nombre,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValueChange = {
                    if(it.length < 50)
                        nombre = it
                },
                label = { Text("Escriba su nombre") },
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(), // que la fila ocupe todo el ancho disponible
            verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente en el centro
        ){
            Image(painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "Imagen apellido",
                modifier = Modifier.size(70.dp).padding(vertical = 8.dp))
            OutlinedTextField(
                value = apellido,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValueChange = {
                    if(it.length < 50)
                        apellido = it
                },
                label = { Text("Escriba su apellido") },
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(), //que la fila ocupe todo el ancho disponible
            verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente en el centro
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Imagen sexo",
                modifier = Modifier.size(70.dp).padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Añade un espacio entre la imagen y el texto
            Text("Sexo: ")
        }
        Row(
            modifier = Modifier.fillMaxWidth(), //que la fila ocupe todo el ancho disponible
            verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente en el centro
        ){
            Image(painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "Imagen de nacimiento",
                modifier = Modifier.size(70.dp).padding(vertical = 8.dp))
            Text("Fecha de nacimiento: ")
            DatePickerDocked()

        }
        Row(
            modifier = Modifier.fillMaxWidth(), //que la fila ocupe todo el ancho disponible
            verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente en el centro
        ){
            Image(painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "Imagen escolaridad",
                modifier = Modifier.size(70.dp).padding(vertical = 8.dp))
            Text("Grado de Escolaridad ")

        }

        Button(
            modifier = Modifier.background(Color.Green),
            onClick = {}
        ) {
            Text("Siguiente")
        }
    }
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