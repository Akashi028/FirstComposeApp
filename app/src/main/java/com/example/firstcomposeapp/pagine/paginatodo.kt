package com.example.firstcomposeapp.pagine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


data class Promemoria(
    val titolo: String,
    val descrizione: String?,
    val completato: Boolean
)

class ToDoElementi : ViewModel(){
    val elementiPromemoria = mutableStateListOf(
            Promemoria("Portare fuori il cane", "", false),
            Promemoria("Portare il gatto", "", false),
            Promemoria("Portare il gatto", "", false)
    )

    fun aggiungiPromemoria(titolo: String) {
        if (titolo.isNotBlank()) {
            elementiPromemoria.add(Promemoria(titolo = titolo, descrizione = "", completato = false))
        }
    }
}

@Composable
fun PaginaToDo(
    viewModel: ToDoElementi = viewModel()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(viewModel.elementiPromemoria) { el ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .clip(RoundedCornerShape(40))
                .border(2.dp, color = Color.Black, shape = RoundedCornerShape(40))
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = el.titolo,
                    fontSize = 20.sp
                )
            }
        }
    }
}


@Composable
fun PopupCreaTodo(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var nomeEvento by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            //usePlatformDefaultWidth = false // Permette al dialog di andare a tutto schermo se serve
        )
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(15.dp)
        ) {
            Column {
                Box {
                    Text("Nuovo Promemoria")
                }
                Row {
                    OutlinedTextField(
                        value = nomeEvento,
                        onValueChange = { nomeEvento = it },
                        label = { Text("Nome evento") }
                    )
                }
                Button(
                    onClick = {
                        if(nomeEvento!=""){
                            onSave(nomeEvento)
                            onDismiss()
                        }
                    }
                ) {
                    Text("Salva")
                }
            }
        }
    }
}
