package com.example.planup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planup.ui.screens.HomeScreen
import com.example.planup.ui.task.model.TaskViewModel
import com.example.planup.ui.task.model.TaskViewModelFactory
import com.example.planup.ui.task.screens.SaveTask
import com.example.planup.ui.task.screens.TaskList
import com.example.planup.ui.theme.PlanUpTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            PlanUpTheme {
                MainScreen()
            }
        }

    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        val taskViewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory())
        Scaffold { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "task_list"
            ) {

                composable("home_screen"){
                    HomeScreen(navController, innerPadding)
                }
                composable("task_list") {
                    TaskList(navController, taskViewModel)
                }
                composable("save_task") {
                    SaveTask(navController, taskViewModel)
                }
            }
        }
    }
}
