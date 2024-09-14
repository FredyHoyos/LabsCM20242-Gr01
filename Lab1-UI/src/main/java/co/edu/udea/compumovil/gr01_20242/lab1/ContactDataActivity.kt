package co.edu.udea.compumovil.gr01_20242.lab1

import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// --- Retrofit Configuration ---

// Data Classes para la respuesta de Geonames
data class GeonamesResponse(val geonames: List<Geoname>)
data class Geoname(val name: String)

// Interfaz de la API de Geonames
interface GeonamesApiService {
    @GET("searchJSON")
    suspend fun getCities(
        @retrofit2.http.Query("country") country: String = "CO",
        @retrofit2.http.Query("featureClass") featureClass: String = "P", // P para poblaciones
        @retrofit2.http.Query("maxRows") maxRows: Int = 1000,
        @retrofit2.http.Query("username") username: String = "daniel22" // Tu nombre de usuario de Geonames
    ): GeonamesResponse
}

// Objeto singleton para Retrofit
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

// Definición de la actividad ContactDataActivity como clase
class ContactDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ContactDataScreen() // Llama a la función @Composable que define la UI
            }
        }
    }
}

@Composable
fun ContactDataScreen() {
    // Estado para los campos de entrada
    var phone by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var country by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }

    val countries = listOf(
        "Argentina", "Bolivia", "Brazil", "Chile", "Colombia",
        "Ecuador", "Paraguay", "Peru", "Uruguay", "Venezuela"
    )

    // Estado para las ciudades obtenidas de la API
    var citiesFromApi by remember { mutableStateOf(listOf<String>()) }
    var isLoading by remember { mutableStateOf(false) }
    var apiError by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    // Llamada a la API para obtener ciudades
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val response = RetrofitClient.apiService.getCities(
                username = "daniel22" // Reemplaza con tu nombre de usuario de Geonames
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
    ) {
        Text(text = stringResource(R.string.contact_data_title), style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        ContactField(
            value = phone,
            onValueChange = { phone = it },
            label = "Teléfono",
            icon = painterResource(id = R.drawable.phone_icon),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            isError = phone.isEmpty()
        )

        Spacer(modifier = Modifier.height(8.dp))

        ContactField(
            value = address,
            onValueChange = { address = it },
            label = "Dirección",
            icon = painterResource(id = R.drawable.address_icon),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ContactField(
            value = email,
            onValueChange = { email = it },
            label = "Correo",
            icon = painterResource(id = R.drawable.email_icon),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            isError = email.isEmpty()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de autocompletado para País
        AutoCompleteTextField(
            value = country,
            onValueChange = { country = it },
            suggestions = countries,
            label = "País",
            icon = painterResource(id = R.drawable.country_icon),
            isError = country.isEmpty()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de autocompletado para Ciudad usando datos de la API
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
                label = "Ciudad",
                icon = painterResource(id = R.drawable.city_icon)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para continuar con la validación
        Button(
            onClick = {
                if (phone.isEmpty() || email.isEmpty() || country.isEmpty()) {
                    Log.d("ContactData", "Faltan campos por completar")
                } else {
                    Log.d(
                        "ContactData",
                        "Información de contacto: Teléfono: $phone, Dirección: $address, Email: $email, País: $country, Ciudad: $city"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Siguiente")
        }
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
                .size(24.dp)
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
                    .size(24.dp)
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











