package com.example.planup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.planup.repository.ProjectRepository
import com.example.planup.repository.TaskRepository
import com.example.planup.ui.screens.MainScreen
import com.example.planup.ui.theme.PlanUpTheme

class MainActivity : ComponentActivity() {

    private val taskRepository = TaskRepository()
    private val projectRepository = ProjectRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PlanUpTheme {
                MainScreen(projectRepository)
            }
        }
    }
}
