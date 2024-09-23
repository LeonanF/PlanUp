package com.example.planup.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.planup.model.Project

@Composable
fun ProjectScreen(navController: NavHostController, project: Project) {
    val context = LocalContext.current
    Toast.makeText(context, "Project: ${project.name}", Toast.LENGTH_SHORT).show()
}