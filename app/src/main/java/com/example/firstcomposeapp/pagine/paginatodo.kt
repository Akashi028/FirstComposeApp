package com.example.firstcomposeapp.pagine

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun PaginaToDo() {
    // Un contenitore che riempie tutto lo schermo e centra il testo
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Questa è la mia pagina di prova!",
            fontSize = 20.sp
        )
    }
}