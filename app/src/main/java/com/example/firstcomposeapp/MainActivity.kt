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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.firstcomposeapp.pagine.Greeting
import com.example.firstcomposeapp.pagine.PaginaToDo


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}



data class NavItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
)
@Composable
fun NavBar(modifier: Modifier = Modifier, navController: NavController){
    val formaBarra = RoundedCornerShape(50)
    val elementiNavBar = listOf(
        NavItem("Calendario", icon = ImageVector.vectorResource(id = R.drawable.ic_custom_home), route = "calendario"),
        NavItem("To-Do List", icon = ImageVector.vectorResource(id = R.drawable.ic_custom_home), route = "ToDo")
    )
    val dimensioneNavBar = (elementiNavBar.size * 100).dp;
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
                        .background(
                            color = if(currentRoute==el.route){
                                        MaterialTheme.colorScheme.primary
                                    }else{
                                        Color.Transparent
                                    })
                        .clickable{
                            navController.navigate(el.route)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
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

@Composable
fun AddEventButton(){
    FloatingActionButton(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(50)),
        onClick = {},
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.add_button),
            contentDescription = "Aggiungi evento",
            Modifier.size(50.dp)
        )
    }
}

@Preview(showBackground = true)
//@PreviewScreenSizes
@Composable
fun MainScreen() {
    FirstComposeAppTheme {
        val navController = rememberNavController();
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {
                NavBar(navController = navController)
            },
            floatingActionButton = {
                AddEventButton()
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "calendario",
                modifier = Modifier.padding(innerPadding)
            ){
                composable("calendario"){
                    Greeting(name = "Ripensiamoci")
                }
                composable("ToDo"){
                    PaginaToDo()
                }
            }
        }
    }
}
