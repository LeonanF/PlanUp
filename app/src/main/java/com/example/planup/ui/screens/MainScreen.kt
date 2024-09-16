package com.example.planup.ui.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planup.repository.ProjectRepository


@Composable
fun MainScreen(projectRepository: ProjectRepository) {

    val navController = rememberNavController()

    projectRepository.PrintProjects()

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