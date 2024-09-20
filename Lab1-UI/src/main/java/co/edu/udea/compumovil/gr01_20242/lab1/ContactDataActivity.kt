package co.edu.udea.compumovil.gr01_20242.lab1

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.edu.udea.compumovil.gr01_20242.lab1.ui.theme.Labs20242Gr01Theme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// --- Retrofit Configuration Dor ---

data class GeonamesResponse(val geonames: List<Geoname>)
data class Geoname(val name: String)

interface GeonamesApiService {
    @GET("searchJSON")
    suspend fun getCities(
        @retrofit2.http.Query("country") country: String = "CO",
        @retrofit2.http.Query("featureClass") featureClass: String = "P",
        @retrofit2.http.Query("maxRows") maxRows: Int = 1000,
        @retrofit2.http.Query("username") username: String = "daniel22"
    ): GeonamesResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://secure.geonames.org/"
    val apiService: GeonamesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeonamesApiService::class.java)
    }
}

class ContactDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val logMessage = intent.getStringExtra("logMessage") // Obtener el mensaje de registro del intent
        setContent {
            MaterialTheme {
                if (logMessage != null) {
                    ContactDataScreen(intent = intent, logMessagePersonal = logMessage)
                }
            }
        }
    }
}


@Composable
fun ContactDataScreen(intent: Intent, logMessagePersonal: String) {
    val messageFromIntent = intent.getStringExtra("logMessagePersonal")
    var phone by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var country by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    val fillRequiredFields = stringResource(id = R.string.fill_required_fields) // Mensaje de advertencia
    val contactData = stringResource(id = R.string.contact_data) // informacion de contacto
    val log_phone= stringResource(id = R.string.phone_label)// log del phone
    val log_address=stringResource(id = R.string.address_label) // Log de la dirección
    val log_email=stringResource(id = R.string.email_label) // Log del email
    val log_country=stringResource(id = R.string.country_label) // Log del pais
    val log_city=stringResource(id = R.string.city_label) // Log del la ciudad

    val countries = listOf(
        "Argentina", "Bolivia", "Brazil", "Chile", "Colombia",
        "Ecuador", "Paraguay", "Peru", "Uruguay", "Venezuela"
    )

    var citiesFromApi by remember { mutableStateOf(listOf<String>()) }
    var isLoading by remember { mutableStateOf(false) }
    var apiError by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val response = RetrofitClient.apiService.getCities(
                username = "daniel22"
            )
            citiesFromApi = response.geonames.map { it.name }
            Log.d("ContactData", "Ciudades obtenidas: ${citiesFromApi.size}")
        } catch (e: Exception) {
            Log.e("ContactData", "Error al obtener ciudades: ${e.message}")
            apiError = "Error al cargar ciudades"
        }
        isLoading = false
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(stringResource(R.string.contact_data_title), style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        ContactField(
            value = phone,
            onValueChange = { phone = it },
            label = stringResource(R.string.phone_label),
            icon = painterResource(id = R.drawable.phone_icon),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            isError = phone.isEmpty()
        )

        Spacer(modifier = Modifier.height(8.dp))

        ContactField(
            value = address,
            onValueChange = { address = it },
            label = stringResource(R.string.address_label),
            icon = painterResource(id = R.drawable.address_icon),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ContactField(
            value = email,
            onValueChange = { email = it },
            label = stringResource(R.string.email_label),
            icon = painterResource(id = R.drawable.email_icon),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            isError = email.isEmpty()
        )

        Spacer(modifier = Modifier.height(8.dp))

        AutoCompleteTextField(
            value = country,
            onValueChange = { country = it },
            suggestions = countries,
            label = stringResource(R.string.country_label),
            icon = painterResource(id = R.drawable.country_icon),
            isError = country.isEmpty()
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        } else if (apiError != null) {
            Text(
                text = apiError ?: "Error desconocido",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            AutoCompleteTextField(
                value = city,
                onValueChange = { city = it },
                suggestions = citiesFromApi,
                label = stringResource(R.string.city_label),
                icon = painterResource(id = R.drawable.city_icon)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        var showDialog by remember { mutableStateOf(false) }
        var logMessage by remember { mutableStateOf("") }

        Button(
            onClick = {
                if (phone.isEmpty() || email.isEmpty() || country.isEmpty()) {
                    Log.d("ContactData", fillRequiredFields) // Mensaje de advertencia si los campos están vacíos
                } else {
                    // Construye el mensaje de registro
                    logMessage = "\n$contactData \n $log_phone: $phone, \n$log_address: $address, \n$log_email: $email, \n$log_country: $country, \n$log_city: $city"
                    Log.d("ContactData", logMessage)

                    if (messageFromIntent != null) {
                        Log.d("Data", messageFromIntent)
                    }
                    Log.d("Data", logMessagePersonal)

                    // Muestra el diálogo
                    showDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.next_button))
        }

        // AlertDialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Información") },
                text = { Text(logMessagePersonal+logMessage) }, // Muestra el logMessage en el diálogo
                confirmButton = {
                    Button(onClick = {
                        // Acción al confirmar
                        showDialog = false
                        // Aquí puedes agregar la lógica adicional que necesites al confirmar
                    }) {
                        Text("Cerrar")
                    }
                }/*,
                dismissButton = {
                    Button(onClick = {
                        // Acción al cancelar
                        showDialog = false
                    }) {
                        Text("Cancelar")
                    }
                }*/
            )
        }
    }
}

@Composable
fun MyAlertDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    message: String
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Título") },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
fun ContactField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: Painter,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .padding(end = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            keyboardOptions = keyboardOptions,
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AutoCompleteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    suggestions: List<String>,
    label: String,
    icon: Painter,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    var filteredSuggestions by remember { mutableStateOf(listOf<String>()) }
    var showSuggestions by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 8.dp)
            )
            OutlinedTextField(
                value = value,
                onValueChange = { newText ->
                    onValueChange(newText)
                    filteredSuggestions = if (newText.isNotEmpty()) {
                        suggestions.filter { it.startsWith(newText, ignoreCase = true) }
                    } else {
                        emptyList()
                    }
                    showSuggestions = newText.isNotEmpty()
                },
                label = { Text(label) },
                isError = isError,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (showSuggestions && filteredSuggestions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 150.dp)
                    .fillMaxWidth()
            ) {
                items(filteredSuggestions.size) { index ->
                    Text(
                        text = filteredSuggestions[index],
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onValueChange(filteredSuggestions[index])
                                showSuggestions = false
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}












