package co.edu.udea.compumovil.gr01_20242.lab1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.edu.udea.compumovil.gr01_20242.lab1.ui.theme.Labs20242Gr01Theme
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.window.Dialog

class PersonalDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilitar el modo de pantalla completa sin bordes
        setContent {
            Labs20242Gr01Theme {
                Column {
                    Titulo() // Función para mostrar la barra de título
                    Cuerpo() // Función para mostrar el cuerpo principal del formulario
                }
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
fun Titulo() {
    // TopAppBar es la barra superior, en este caso con un fondo azul y un título
    TopAppBar(
        title = { Text(stringResource(id = R.string.personal_data_title)) }, // Texto del título en la barra superior reemplazado por `stringResource()`
        modifier = Modifier.background(Color.Blue, RectangleShape),
    )
}

@Composable
fun Cuerpo() {
    val configuration = LocalConfiguration.current

    var name by rememberSaveable { mutableStateOf("") } // Variable para almacenar el nombre
    var last_name by rememberSaveable { mutableStateOf("") } // Variable para almacenar el apellido
    var sex by rememberSaveable { mutableStateOf("Man") } // Variable para almacenar el sexo, valor inicial "Man"
    var select_date by rememberSaveable { mutableStateOf<String?>(null) } // Variable para almacenar la fecha de nacimiento
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) } // Variable para almacenar el grado académico seleccionado
    val context = LocalContext.current // Contexto para iniciar nuevas actividades
    val fillRequiredFields = stringResource(id = R.string.fill_required_fields) // Mensaje de advertencia
    val personalInformation = stringResource(id = R.string.personal_information)
    val log_name= stringResource(id = R.string.name)// Log del nombre
    val log_last_name=stringResource(id = R.string.last_name) // Log del apellido
    val log_sex=stringResource(id = R.string.sex) // Log del sexo
    val log_date=stringResource(id = R.string.date) // Log de la fecha de nacimiento
    val log_degree=stringResource(id = R.string.degree) // Log del grado académico


    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Fila para el campo de "Name"
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.usuario), // Imagen de usuario
                    contentDescription = stringResource(id = R.string.image_name), // Descripción accesible usando `stringResource()`
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = name,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = { if (it.length < 50) name = it }, // Actualiza el valor del nombre
                    label = { Text(stringResource(id = R.string.name)) }, // Etiqueta del campo usando `stringResource()`
                    modifier = Modifier.weight(1f),
                    isError = name.isEmpty()
                )
                Spacer(modifier = Modifier.width(60.dp)) // Espacio de 60dp de ancho
                Image(
                    painter = painterResource(id = R.drawable.mas), // Imagen de apellido
                    contentDescription = stringResource(id = R.string.image_last_name), // Descripción accesible usando `stringResource()`
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = last_name,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = { if (it.length < 50) last_name = it }, // Actualiza el valor del apellido
                    label = { Text(stringResource(id = R.string.last_name)) }, // Etiqueta del campo usando `stringResource()`
                    modifier = Modifier.weight(1f),
                    isError = last_name.isEmpty()
                )
            }

            // Fila para la selección de "Sex"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dos), // Imagen para el sexo
                    contentDescription = stringResource(id = R.string.image_sex), // Descripción accesible usando `stringResource()`
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                SeleccionSexo(onSelectionChange = { selected -> sex = selected }) // Componente para la selección de sexo
            }

            // Fila para la selección de "Date of Birth"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.calendario), // Imagen de calendario
                    contentDescription = stringResource(id = R.string.image_birth), // Descripción accesible usando `stringResource()`
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(8.dp)) // Espacio de 8dp de ancho
                Text(stringResource(id = R.string.date_text))
                Spacer(modifier = Modifier.width(20.dp))
                DatePickerModal(onDateSelected = { date -> select_date = date }) // Componente para seleccionar la fecha de nacimiento
            }

            // Fila para la selección de "Education Level"
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gorro), // Imagen de gorro académico
                    contentDescription = stringResource(id = R.string.image_schooling), // Descripción accesible usando `stringResource()`
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(30.dp)) // Espacio de 30dp de ancho
                ListaGrado(onOptionSelected = { option -> selectedOption = option }) // Componente para seleccionar el grado académico
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón "Next" que valida los campos antes de continuar
            Button(
                onClick = {
                    if (name.isEmpty() || last_name.isEmpty() || select_date.isNullOrEmpty()) {
                        Log.d("Data", fillRequiredFields) // Mensaje de advertencia si los campos están vacíos usando `stringResource()`
                    } else {
                        val logMessage = "$personalInformation -> $log_name: $name, $log_last_name: $last_name, $log_sex: $sex, $log_date: $select_date, $log_degree: $selectedOption"
                        Log.d("Data", logMessage)
                        val intent = Intent(context, ContactDataActivity::class.java) // Intenta ir a la siguiente actividad
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(id = R.string.next)) // Texto en el botón usando `stringResource()`
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Fila para el campo de "Name"
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.usuario), // Imagen de usuario
                    contentDescription = stringResource(id = R.string.image_name), // Descripción accesible usando `stringResource()`
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = name,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = { if (it.length < 50) name = it }, // Actualiza el valor del nombre
                    label = { Text(stringResource(id = R.string.name)) }, // Etiqueta del campo usando `stringResource()`
                    modifier = Modifier.weight(1f),
                    isError = name.isEmpty()
                )
            }

            // Fila para el campo de "Last Name"
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mas), // Imagen de apellido
                    contentDescription = stringResource(id = R.string.image_last_name), // Descripción accesible usando `stringResource()`
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = last_name,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = { if (it.length < 50) last_name = it }, // Actualiza el valor del apellido
                    label = { Text(stringResource(id = R.string.last_name)) }, // Etiqueta del campo usando `stringResource()`
                    modifier = Modifier.weight(1f),
                    isError = last_name.isEmpty()
                )
            }

            // Fila para la selección de "Sex"
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dos), // Imagen para el sexo
                    contentDescription = stringResource(id = R.string.image_sex), // Descripción accesible usando `stringResource()`
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                SeleccionSexo(onSelectionChange = { selected -> sex = selected }) // Componente para la selección de sexo
            }

            // Fila para la selección de "Date of Birth"
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.calendario), // Imagen de calendario
                    contentDescription = stringResource(id = R.string.image_birth), // Descripción accesible usando `stringResource()`
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(8.dp)) // Espacio de 8dp de ancho
                Text(stringResource(id = R.string.date_text))
                Spacer(modifier = Modifier.width(8.dp))
                DatePickerModal(onDateSelected = { date -> select_date = date }) // Componente para seleccionar la fecha de nacimiento
            }

            // Fila para la selección de "Education Level"
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gorro), // Imagen de gorro académico
                    contentDescription = stringResource(id = R.string.image_schooling), // Descripción accesible usando `stringResource()`
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(30.dp)) // Espacio de 30dp de ancho
                ListaGrado(onOptionSelected = { option -> selectedOption = option }) // Componente para seleccionar el grado académico
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón "Next" que valida los campos antes de continuar
            Button(
                onClick = {
                    if (name.isEmpty() || last_name.isEmpty() || select_date.isNullOrEmpty()) {
                        Log.d("Data", fillRequiredFields) // Mensaje de advertencia si los campos están vacíos usando `stringResource()`
                    } else {
                        val logMessage = "$personalInformation -> $log_name: $name, $log_last_name: $last_name, $log_sex: $sex, $log_date: $select_date, $log_degree: $selectedOption"
                        Log.d("Data", logMessage)
                        val intent = Intent(context, ContactDataActivity::class.java) // Intenta ir a la siguiente actividad
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(id = R.string.next)) // Texto en el botón usando `stringResource()`
            }
        }
    }

}

@Composable
fun SeleccionSexo(
    modifier: Modifier = Modifier,
    onSelectionChange: (String) -> Unit
) {
    // Lista de opciones de sexo
    val opcionesSexo = listOf(stringResource(id = R.string.man), stringResource(id = R.string.woman)) // Texto de opciones usando `stringResource()`
    var seleccion by rememberSaveable { mutableStateOf(opcionesSexo.first()) } // Opción seleccionada inicialmente "Man"

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        opcionesSexo.forEach { opcion ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    seleccion = opcion
                    onSelectionChange(opcion) // Cambia la selección cuando se hace clic
                }
            ) {
                RadioButton(
                    selected = (seleccion == opcion), // Verifica si la opción es seleccionada
                    onClick = {
                        seleccion = opcion
                        onSelectionChange(opcion) // Cambia la opción seleccionada
                    }
                )
                Text(text = opcion) // Muestra el texto de la opción
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaGrado(
    modifier: Modifier = Modifier,
    onOptionSelected: (String?) -> Unit
) {
    val options = listOf(
        stringResource(id = R.string.primary),
        stringResource(id = R.string.secondary),
        stringResource(id = R.string.university),
        stringResource(id = R.string.other)
    )
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.requiredWidth(250.dp) // Ancho mínimo de 150dp

    ) {
        TextField(
            readOnly = true,
            value = selectedOption ?: stringResource(id = R.string.select_education_level),
            onValueChange = {},
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .menuAnchor()
                .clickable { expanded = true }
        )
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
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit
) {
    // Modal para seleccionar la fecha de nacimiento usando un DatePicker
    val showDialog =
        rememberSaveable { mutableStateOf(false) } // Controla si el diálogo está visible
    val datePickerState = rememberDatePickerState() // Estado del DatePicker
    val formatter = rememberSaveable {
        SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )
    } // Formato de la fecha

    val selectedDate = datePickerState.selectedDateMillis?.let {
        formatter.format(Date(it)) // Formatea la fecha seleccionada
    } ?: ""

    LaunchedEffect(datePickerState.selectedDateMillis) {
        val newDate = datePickerState.selectedDateMillis?.let {
            formatter.format(Date(it)) // Actualiza la fecha cuando el usuario selecciona una nueva fecha
        } ?: ""
        onDateSelected(newDate) // Llama a la función callback con la fecha seleccionada
        if (datePickerState.selectedDateMillis != null) {
            showDialog.value = false // Cierra el diálogo si se seleccionó una fecha
        }
    }

    Box{
        OutlinedTextField(
            value = selectedDate, // Muestra la fecha seleccionada
            onValueChange = { },
            label = { Text(stringResource(id = R.string.date)) }, // Etiqueta del campo usando `stringResource()`
            readOnly = true,
            isError = selectedDate.isEmpty(),
            trailingIcon = {
                IconButton(onClick = { showDialog.value = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange, // Icono de calendario
                        contentDescription = stringResource(id = R.string.date_of_birth_icon) // Descripción accesible usando `stringResource()`
                    )
                }
            },
            modifier = Modifier
                .width(160.dp)
                .height(60.dp)
        )
        if (showDialog.value) {
            Dialog(onDismissRequest = { showDialog.value = false }) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxWidth(1f)
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        DatePicker(
                            state = datePickerState,
                            showModeToggle = false // Oculta el botón de alternar modos
                        )
                    }

                }
            }
        }
    }
}

