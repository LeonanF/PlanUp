package com.example.planup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planup.repository.TaskRepository
import com.example.planup.ui.screens.HomeScreen
import com.example.planup.ui.theme.PlanUpTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val taskRepository = TaskRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PlanUpTheme {
                MainScreen(taskRepository)
            }
        }

    }

    @Composable
    fun MainScreen(taskRepository: TaskRepository) {

        val navController = rememberNavController()

        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                taskRepository.fetchTasks { result ->
                    result.onSuccess { tasks ->
                        for(task in tasks){
                            println(task.name+": "+ task.description)
                        }
                    }.onFailure { throwable ->
                        println(throwable.message)
                    }
                }
            }
        }

        Scaffold { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home_screen"
            ) {

                composable("home_screen"){
                    HomeScreen(navController, innerPadding)
                }

            }
        }
    }
}
