package co.edu.udea.compumovil.gr01_20242.lab1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import co.edu.udea.compumovil.gr01_20242.lab1.ui.theme.Labs20242Gr01Theme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.Intent
import android.content.Context
import android.content.res.Configuration
import androidx.compose.ui.window.Dialog


class PersonalDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labs20242Gr01Theme {
                Column {
                    Titulo()
                    Cuerpo()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Titulo() {
    TopAppBar(
        title = { Text("Información Personal")},
        modifier = Modifier.background(Color.Blue, RectangleShape),
        // Cambia a Color.Blue para fondo azul
    )

}



@Composable
fun Cuerpo() {
    // rememberSaveable
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellido by rememberSaveable { mutableStateOf("") }
    var sexo by rememberSaveable { mutableStateOf("Hombre") }
    var fecha by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) }
    val context = LocalContext.current


    BoxWithConstraints() {
        // Comprobar si la anchura disponible es mayor que la altura
        val isLandscape = maxWidth > maxHeight

        // Disposición en función de la orientación
        if (isLandscape) {
            // En modo paisaje, colocar los componentes en fila
            Row(
                modifier = Modifier.fillMaxWidth(), // que la fila ocupe todo el ancho disponible
                verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente en el centro
            ){
                Image(painter = painterResource(id = R.drawable.usuario),
                    contentDescription = "Imagen nombre",
                    modifier = Modifier.size(60.dp).padding(vertical = 8.dp))
                OutlinedTextField(
                    value = nombre,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrect = false
                    ),
                    onValueChange = {
                        if(it.length < 50)
                            nombre = it
                    },
                    label = { Text("Escriba su nombre") },
                )
                Image(painter = painterResource(R.drawable.mas),
                    contentDescription = "Imagen apellido",
                    modifier = Modifier.size(60.dp).padding(vertical = 8.dp))
                OutlinedTextField(
                    value = apellido,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrect = false
                    ),
                    onValueChange = {
                        if(it.length < 50)
                            apellido = it
                    },
                    label = { Text("Escriba su apellido") },
                )
            }
        } else {
            // En modo retrato, colocar los componentes en columna
            Column{
                Row(
                    modifier = Modifier.fillMaxWidth(), // que la fila ocupe todo el ancho disponible
                    verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente en el centro
                ){
                    Image(painter = painterResource(id = R.drawable.usuario),
                        contentDescription = "Imagen nombre",
                        modifier = Modifier.size(60.dp).padding(vertical = 8.dp))
                    OutlinedTextField(
                        value = nombre,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Sentences,
                            autoCorrect = false
                        ),
                        onValueChange = {
                            if(it.length < 50)
                                nombre = it
                        },
                        label = { Text("Escriba su nombre") },
                    )
                }
                Spacer(modifier = Modifier.height(16.dp)) // Espacio entre los componentes
                Row(
                    modifier = Modifier.fillMaxWidth(), // que la fila ocupe todo el ancho disponible
                    verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente en el centro
                ){
                    Image(painter = painterResource(R.drawable.mas),
                        contentDescription = "Imagen apellido",
                        modifier = Modifier.size(60.dp).padding(vertical = 8.dp))
                    OutlinedTextField(
                        value = apellido,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Sentences,
                            autoCorrect = false
                        ),
                        onValueChange = {
                            if(it.length < 50)
                                apellido = it
                        },
                        label = { Text("Escriba su apellido") },
                    )
                }
            }
        }
    }




    Column (
        modifier = Modifier.padding(10.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), //que la fila ocupe todo el ancho disponible
            verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente en el centro
        ) {
            Image(
                painter = painterResource(id = R.drawable.dos),
                contentDescription = "Imagen sexo",
                modifier = Modifier.size(60.dp).padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Añade un espacio entre la imagen y el texto
            Text("Sexo: ")

            SeleccionSexo(
                onSelectionChange = { selected ->
                    sexo = selected
                }
            )

        }

        Row(
            modifier = Modifier.fillMaxWidth(), //que la fila ocupe todo el ancho disponible
            verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente en el centro
        ){
            Image(painter = painterResource(R.drawable.calendario),
                contentDescription = "Imagen de nacimiento",
                modifier = Modifier.size(60.dp).padding(vertical = 8.dp))
            Text("Fecha de nacimiento: ")

            DatePickerModal(
                onDateSelected = { date ->
                    fecha = date
                }
            )


        }
        Row(
            modifier = Modifier.fillMaxWidth(), //que la fila ocupe todo el ancho disponible
            verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente en el centro
        ){
            Image(painter = painterResource(R.drawable.gorro),
                contentDescription = "Imagen escolaridad",
                modifier = Modifier.size(60.dp).padding(vertical = 8.dp))

            ListaGrado(
                onOptionSelected = { option ->
                    selectedOption = option
                }
            )

        }

        Button(modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            onClick = {

                if (nombre.isEmpty() || apellido.isEmpty() || fecha.equals("")){
                    Log.d(
                        "Datos",
                        "Llena todos los campos obligatorios por favor"

                    )
                }else{
                    Log.d(
                        "Datos",
                        "Información personal -> Nombre : $nombre, Apellido $apellido, Sexo: $sexo, Fecha nacimiento: $fecha, Grado: $selectedOption"

                    )
                    val intent = Intent(context, ContactDataActivity::class.java)
                    context.startActivity(intent)

                }
            }
        ) {
            Text("Siguiente")
        }
    }
}

@Composable
fun SeleccionSexo(
    modifier: Modifier = Modifier,
    onSelectionChange: (String) -> Unit // Callback para informar al componente padre
) {
    val sexo = listOf("Hombre", "Mujer")
    var seleccion by rememberSaveable { mutableStateOf(sexo.first()) }

    // Usar Row para colocar los botones de radio horizontalmente
    Row(
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp) // Espaciado entre los elementos
    ) {
        sexo.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    seleccion = item
                    onSelectionChange(item) // Notificar al componente padre sobre la selección
                }
            ) {
                RadioButton(
                    selected = (seleccion == item),
                    onClick = {
                        seleccion = item
                        onSelectionChange(item) // Notificar al componente padre sobre la selección
                    }
                )
                Text(
                    text = item,
                    modifier = Modifier.padding(start = 1.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaGrado(
    modifier: Modifier = Modifier,
    onOptionSelected: (String?) -> Unit // Callback para informar al componente padre
) {
    // Opciones del spinner
    val options = listOf("Primaria", "Secundaria", "Universitaria", "Otro")
    // Estado para mostrar u ocultar el menú desplegable
    var expanded by rememberSaveable { mutableStateOf(false) }
    // Estado para la opción seleccionada
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) } // Inicialmente nulo

    // Contenedor del spinner
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Botón que muestra la opción seleccionada o el texto predeterminado
        TextField(
            readOnly = true,
            value = selectedOption ?: "Grado de escolaridad", // Texto predeterminado si no hay selección
            onValueChange = {},
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Icon"
                )
            },
            modifier = Modifier
                .menuAnchor() // Para alinear el menú desplegable con el botón
                .clickable { expanded = true } // Abre el menú cuando se hace clic en el TextField
        )
        // Menú desplegable
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                        onOptionSelected(option) // Notificar al componente padre sobre la selección
                    }
                )
            }
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
fun DatePickerModal(
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit // Callback para informar al componente padre
) {
    // Controlar la visibilidad del DatePicker en un diálogo
    val showDialog = rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val formatter = rememberSaveable { SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()) }

    // Convertir la fecha seleccionada en formato legible
    val selectedDate = datePickerState.selectedDateMillis?.let {
        formatter.format(Date(it))
    } ?: ""

    // Efecto para actualizar la fecha seleccionada y cerrar el diálogo
    LaunchedEffect(datePickerState.selectedDateMillis) {
        val newDate = datePickerState.selectedDateMillis?.let {
            formatter.format(Date(it))
        } ?: ""
        onDateSelected(newDate)
        if (datePickerState.selectedDateMillis != null) {
            showDialog.value = false // Cierra el diálogo después de seleccionar una fecha
        }
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        // Campo de texto que muestra la fecha seleccionada
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { /* No-op, el campo es solo de lectura */ },
            label = { Text("Fecha") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDialog.value = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Fecha"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        // Mostrar el diálogo modal cuando se hace clic en el campo de fecha
        if (showDialog.value) {
            Dialog(onDismissRequest = { showDialog.value = false }) {
                Box(
                    modifier = Modifier
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
}

