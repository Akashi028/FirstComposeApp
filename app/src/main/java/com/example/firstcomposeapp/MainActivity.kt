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
                .background(Color.Red)
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
        NavItem("Titolo1", icon = ImageVector.vectorResource(id = R.drawable.ic_custom_home)),
        NavItem("Titolo2", icon = ImageVector.vectorResource(id = R.drawable.ic_custom_home)),
        NavItem("Titolo3", icon = ImageVector.vectorResource(id = R.drawable.ic_custom_home))
    )


    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(75.dp)
            .clip(formaBarra)
            .background(color = Color.Red)
            .padding(2.dp),
//            .border(
//                width = 2.dp,
//                color = Color.Blue,
//                shape = formaBarra
//            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly

    ){
        elementiNavBar.forEach { el ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(shape = formaBarra)
                    .background(color = Color(0xFdddee66)),
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

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    FirstComposeAppTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {
                NavBar(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                )
            }
        ) { innerPadding ->
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
