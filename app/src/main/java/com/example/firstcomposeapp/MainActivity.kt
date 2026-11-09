package com.example.firstcomposeapp

import android.app.Dialog
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.firstcomposeapp.pagine.PopupCreaEvento
import com.example.firstcomposeapp.pagine.PaginaCalendario
import com.example.firstcomposeapp.pagine.PaginaToDo
import com.example.firstcomposeapp.pagine.PopupCreaTodo
import com.example.firstcomposeapp.pagine.Promemoria
import com.example.firstcomposeapp.pagine.ToDoElementi
import org.w3c.dom.Text


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
fun AddEventButton(navController: NavController, viewModel: ToDoElementi = viewModel()){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var showPopup by remember { mutableStateOf(false) }

    FloatingActionButton(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(50)),
        onClick = {showPopup = true},
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.add_button),
            contentDescription = "Aggiungi evento",
            Modifier.size(40.dp)
        )
    }

    if(showPopup){
        Dialog(onDismissRequest = {showPopup=false}){
            if(currentRoute == "calendario"){
                PopupCreaEvento(
                    onDismiss = { showPopup = false }
                )
            }
            if(currentRoute == "ToDo"){
                PopupCreaTodo(
                    onDismiss = { showPopup = false },
                    onSave = { testoInserito ->
                        viewModel.aggiungiPromemoria(testoInserito)
                        showPopup = false
                    }
                )
            }
        }
    }
}
@Preview(showBackground = true)
//@PreviewScreenSizes
@Composable
fun MainScreen() {
    FirstComposeAppTheme {
        val navController = rememberNavController()
        val sharedViewModel: ToDoElementi = viewModel()
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {
                NavBar(navController = navController)
            },
            floatingActionButton = {
                AddEventButton(navController, viewModel = sharedViewModel)
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "calendario",
                modifier = Modifier.padding(innerPadding)
            ){
                composable("calendario"){
                    PaginaCalendario(name = "Strano")
                }
                composable("ToDo"){
                    PaginaToDo(viewModel = sharedViewModel)
                }
            }
        }
    }
}
