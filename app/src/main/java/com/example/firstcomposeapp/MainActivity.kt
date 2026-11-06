package com.example.firstcomposeapp

import android.icu.text.CaseMap
import android.os.Bundle
import android.widget.Button
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.firstcomposeapp.ui.theme.FirstComposeAppTheme
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hello $name!"
            )
            Button(onClick = { /* Azione del bottone */ }) {
                Text(text = "Cliccami")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .width(200.dp)
                .height(200.dp)
        ) {
            // Eventuale contenuto del box
        }
    }
}

data class NavItem(
    val title: String,
    val icon: ImageVector
)
@Composable
fun NavBar(modifier: Modifier = Modifier){
    val formaBarra = RoundedCornerShape(50)
    val elementiNavBar = listOf(
        NavItem("Calendario", icon = ImageVector.vectorResource(id = R.drawable.ic_custom_home)),
        NavItem("To-Do List", icon = ImageVector.vectorResource(id = R.drawable.ic_custom_home))
    )
    val dimensioneNavBar = (elementiNavBar.size * 100).dp;

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .width(dimensioneNavBar)
                .height(75.dp)
                .clip(formaBarra)
                .background(color = MaterialTheme.colorScheme.tertiary)
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)

        ){
            elementiNavBar.forEach { el ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(shape = formaBarra)
                        .background(color = MaterialTheme.colorScheme.primary),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                    //contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = el.icon,
                        contentDescription = el.title,
                        modifier = Modifier
                            .size(40.dp)
                    )
                    //Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = el.title,
                        color = Color.Black,
                        fontSize = 11.sp,
                        modifier = Modifier
                            //.align(alignment = Alignment.BottomCenter)
                            .padding(4.dp),
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
//@PreviewScreenSizes
@Composable
fun MainScreen() {
    FirstComposeAppTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {
                NavBar()
            }
        ) { innerPadding ->
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
