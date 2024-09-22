package com.example.planup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planup.ui.screens.CreateProjectScreen
import com.example.planup.ui.screens.CreateTaskScreen
import com.example.planup.ui.screens.HomeScreen
import com.example.planup.ui.screens.LoginScreen
import com.example.planup.ui.screens.ProjectScreen
import com.example.planup.ui.screens.RegisterScreen
import com.example.planup.ui.task.model.TaskViewModel
import com.example.planup.ui.theme.PlanUpTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var innerPadding : PaddingValues

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            PlanUpTheme {
                InitPadding()
                PlanUpNavHost()
            }
        }
    }

    @Composable
    fun InitPadding(){
        Scaffold { innerPadding ->
            this.innerPadding = innerPadding
        }
    }

    @Composable
    fun PlanUpNavHost(){

        navController = rememberNavController()

        NavHost(navController, startDestination = "login_screen"
        ){
            composable("login_screen"){ LoginScreen(navController = navController)}
            composable("register_screen"){ RegisterScreen(navController = navController) }
            composable("create_project_screen"){ CreateProjectScreen(navController = navController ) }
            composable("create_task"){ CreateTaskScreen(navController = navController, viewModel = TaskViewModel()) }
            composable("project_screen"){ ProjectScreen(navController = navController) }
            composable("home_screen"){ HomeScreen(navController = navController)}
        }
    }
}
