package com.example.firstcomposeapp.pagine


import android.app.Application
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstcomposeapp.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Promemoria(
    val id: String = UUID.randomUUID().toString(),
    val titolo: String,
    val data: LocalDate? = null,
    val descrizione: String = "",
    var completato: Boolean = false,
    var isPendingRemoval: Boolean = false,
    val importanza: Int = 0
)

private const val PREF_KEY_NAME = "lista_promemoria_key"
private val prefKey = stringPreferencesKey(PREF_KEY_NAME)

class ToDoElementi(application: Application) : AndroidViewModel(application) {

    private val gson = Gson()

    val elementiPromemoria = mutableStateListOf<Promemoria>()

    init {
        viewModelScope.launch {
            val datiSalvati = caricaDatiDalTelefono()

            if (datiSalvati.isNotEmpty()) {
                elementiPromemoria.addAll(datiSalvati)
            } else {
                // Dati di default se è la primissima apertura
                elementiPromemoria.addAll(
                    listOf(
                        Promemoria(
                            titolo = "Portare fuori il cane",
                            data = LocalDate.of(2026, 11, 15),
                            descrizione = "Ho da portare fuori il cane",
                            completato = false,
                            importanza = 1
                        ),
                        Promemoria(
                            titolo = "Portare il gatto",
                            data = LocalDate.of(2026, 11, 15),
                            descrizione = "Non so se essere contento",
                            completato = false
                        ),
                        Promemoria(
                            titolo = "Portare il cavallo",
                            data = LocalDate.of(2026, 11, 15),
                            descrizione = "Caspita",
                            completato = false
                        ),
                        Promemoria(
                            titolo = "Portare il pesce",
                            data = LocalDate.of(2026, 11, 15),
                            descrizione = "Prova a farlo salire",
                            completato = false,
                            importanza = 2
                        ),
                        Promemoria(
                            titolo = "Portare la scimmia",
                            data = LocalDate.of(2026, 11, 15),
                            descrizione = "Credi sia sensato?",
                            completato = true,
                            importanza = 3
                        ),
                        Promemoria(
                            titolo = "Portare il lupo",
                            data = LocalDate.of(2026, 11, 15),
                            descrizione = "Mia nonna e ciao",
                            completato = true,
                            importanza = 1
                        ),
                    )
                )
            }
        }
    }

    fun aggiungiPromemoria(titolo: String, data: LocalDate, descrizione: String, importanza: Int) {
        if (titolo.isNotBlank()) {
            elementiPromemoria.add(
                Promemoria(
                    titolo = titolo,
                    data = data,
                    descrizione = descrizione,
                    completato = false,
                    importanza = importanza
                )
            )
            salvaDatiSulTelefono()
        }
    }

    fun spuntaPromemoria(id: String) {
        val index = elementiPromemoria.indexOfFirst { it.id == id }
        if (index == -1) return

        val elementoCorrente = elementiPromemoria[index]
        if (elementoCorrente.isPendingRemoval) return

        val elementoAggiornato = elementoCorrente.copy(completato = !elementoCorrente.completato, isPendingRemoval = true)

        elementiPromemoria[index] = elementoAggiornato

        viewModelScope.launch {
            delay(1000)

            val currentIndex = elementiPromemoria.indexOfFirst { it.id == id }
            if (currentIndex != -1) {
                elementiPromemoria[currentIndex] = elementiPromemoria[currentIndex].copy(
                    isPendingRemoval = false
                )
                salvaDatiSulTelefono()
            }
        }
    }

    private fun salvaDatiSulTelefono() {
        viewModelScope.launch {
            val jsonString = gson.toJson(elementiPromemoria.toList())
            getApplication<Application>().applicationContext.dataStore.edit { preferences ->
                preferences[prefKey] = jsonString
            }
        }
    }

    private suspend fun caricaDatiDalTelefono(): List<Promemoria> {
        return try {
            val preferences = getApplication<Application>().applicationContext.dataStore.data.first()
            val jsonString = preferences[prefKey] ?: return emptyList()
            val type = object : TypeToken<List<Promemoria>>() {}.type
            gson.fromJson(jsonString, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }
}

@Composable
fun HeaderPagina(
    indiceSelezionato: String,
    onCambiaIndice: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .background(Color.Gray)
            .statusBarsPadding(),
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = "Promemoria",
                color = Color.White,
                fontSize = 30.sp
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .border(1.dp, Color.White, RoundedCornerShape(50))
                        .background(if (indiceSelezionato == "Da fare") Color(0x44FFFFFF) else Color.Transparent)
                        .clickable { onCambiaIndice("Da fare") }
                        .padding(horizontal = 15.dp, vertical = 5.dp),
                ) {
                    Text("Da fare", color = Color.White, letterSpacing = 0.sp)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .border(1.dp, Color.White, RoundedCornerShape(50))
                        .background(if (indiceSelezionato == "Completati") Color(0x44FFFFFF) else Color.Transparent)
                        .clickable { onCambiaIndice("Completati") }
                        .padding(horizontal = 15.dp, vertical = 5.dp),
                ) {
                    Text("Completati", color = Color.White, letterSpacing = 0.sp)
                }
            }
        }
    }
}

@Composable
fun ToDoNonCompletati(
    viewModel: ToDoElementi
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
    ) {
        items(
            items = viewModel.elementiPromemoria.filter { (!it.completato && !it.isPendingRemoval) || (it.completato && it.isPendingRemoval) },
            key = { it.id }
        ) { el ->
            Row(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth()
                    .height(75.dp)
                    .clip(RoundedCornerShape(40))
                    .background(color = MaterialTheme.colorScheme.surface)
                    .border(1.dp, color = Color.Black, shape = RoundedCornerShape(40))
                    .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = el.completato,
                    onCheckedChange = {
                        if (!el.isPendingRemoval) {
                            viewModel.spuntaPromemoria(el.id)
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = Color.Red,
                        checkedColor = Color.Red
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = el.titolo,
                        fontSize = 20.sp,
                        color = if (el.completato) Color.Gray else Color.Black
                    )
                    if (el.descrizione.isNotBlank()) {
                        Text(
                            text = el.descrizione,
                            fontSize = 15.sp,
                            color = if (el.completato) Color.Gray else Color.Black
                        )
                    }
                }
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = when (el.importanza) {
                            3 -> R.drawable.redcircle
                            2 -> R.drawable.yellowcircle
                            1 -> R.drawable.greencircle
                            else -> R.drawable.whitecircle
                        }
                    ),
                    contentDescription = "Importanza",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = el.data?.format(formatter) ?: "Nessuna data",
                    letterSpacing = 0.sp,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun ToDoCompletati(
    viewModel: ToDoElementi
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
    ) {
        items(
            items = viewModel.elementiPromemoria.filter { (it.completato && !it.isPendingRemoval) || (!it.completato && it.isPendingRemoval) },
            key = { it.id }
        ) { el ->
            Row(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth()
                    .height(75.dp)
                    .clip(RoundedCornerShape(40))
                    .background(color = MaterialTheme.colorScheme.surface)
                    .border(1.dp, color = Color.Black, shape = RoundedCornerShape(40))
                    .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = el.completato,
                    onCheckedChange = {
                        viewModel.spuntaPromemoria(el.id)
                    },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = Color.Red,
                        checkedColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = el.titolo,
                        fontSize = 20.sp,
                        color = Color.Gray
                    )
                    if (el.descrizione.isNotBlank()) {
                        Text(
                            text = el.descrizione,
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                    }
                }

                Text(
                    text = el.data?.format(formatter) ?: "Nessuna data",
                    letterSpacing = 0.sp,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun PaginaToDo(viewModel: ToDoElementi) {
    var indice by remember { mutableStateOf("Da fare") }
    Column {
        HeaderPagina(
            indiceSelezionato = indice,
            onCambiaIndice = { nuovoIndice ->
                indice = nuovoIndice
            }
        )
        if (indice == "Completati") {
            ToDoCompletati(viewModel)
        } else {
            ToDoNonCompletati(viewModel)
        }
    }
}

@Composable
fun PopupCreaTodo(
    onDismiss: () -> Unit,
    onSave: (String, LocalDate, String, Int) -> Unit
) {
    var titolo by remember { mutableStateOf("") }
    var descrizione by remember { mutableStateOf("") }
    var dataSelezionata by remember { mutableStateOf(LocalDate.now()) }
    var mostraDatePicker by remember { mutableStateOf(false) }
    var nuovaImportanza by remember { mutableIntStateOf(0) }
    var iconaImportanza by remember { mutableIntStateOf(R.drawable.down_arrow) }
    val formatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuovo Promemoria") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = titolo,
                    onValueChange = { titolo = it },
                    label = { Text("Cosa devi ricordare?") },
                    trailingIcon = {
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = iconaImportanza),
                                    contentDescription = "Espandi",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                Text("Importanza:")
                                DropdownMenuItem(
                                    text = { Text("Nessuna importanza") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.whitecircle),
                                            contentDescription = "Nessuna importanza",
                                            tint = Color.Unspecified,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    onClick = {
                                        nuovaImportanza = 0
                                        expanded = false
                                        iconaImportanza = R.drawable.whitecircle
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Bassa") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.greencircle),
                                            contentDescription = "Importanza bassa",
                                            tint = Color.Unspecified,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    onClick = {
                                        nuovaImportanza = 1
                                        expanded = false
                                        iconaImportanza = R.drawable.greencircle
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Media") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.yellowcircle),
                                            contentDescription = "Importanza media",
                                            tint = Color.Unspecified,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    onClick = {
                                        nuovaImportanza = 2
                                        expanded = false
                                        iconaImportanza = R.drawable.yellowcircle
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Alta") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.redcircle),
                                            contentDescription = "Importanza alta",
                                            tint = Color.Unspecified,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    onClick = {
                                        nuovaImportanza = 3
                                        expanded = false
                                        iconaImportanza = R.drawable.redcircle
                                    }
                                )
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descrizione,
                    onValueChange = { descrizione = it },
                    placeholder = { Text("Descrizione") },
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = dataSelezionata.format(formatter),
                        onValueChange = {},
                        enabled = false,
                        label = { Text("Giorno") },
                        trailingIcon = {
                            Icon(Icons.Default.DateRange, contentDescription = null)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { mostraDatePicker = true }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titolo.isNotBlank()) {
                        onSave(titolo, dataSelezionata, descrizione, nuovaImportanza)
                        onDismiss()
                    }
                }
            ) {
                Text("Salva")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla")
            }
        }
    )

    if (mostraDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = dataSelezionata
                .atStartOfDay(ZoneId.of("UTC"))
                .toInstant()
                .toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { mostraDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            dataSelezionata = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                        }
                        mostraDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostraDatePicker = false }) {
                    Text("Annulla")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

private val Context.dataStore by preferencesDataStore(name = "preferenze_promemoria")
