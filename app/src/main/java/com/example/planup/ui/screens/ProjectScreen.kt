package com.example.planup.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.planup.model.Project
import com.example.planup.repository.ProjectRepository
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProjectScreen(navController: NavHostController) {

    val projects = remember { mutableStateOf<List<Project>?>(null) }
    val error = remember { mutableStateOf<String?>(null) }

    val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

    LaunchedEffect(userId) {
        ProjectRepository().fetchUserProjects(userId) { result, errorMsg ->
            projects.value = result
            error.value = errorMsg
        }
    }

    Column {
        error.value?.let {
            Text("Erro: $it", color = Color.Red)
        }

        projects.value?.let { projectList ->
            LazyColumn {
                items(projectList) { project ->
                    Text(project.name)
                }
            }
        } ?: run {
            Text("Carregando...")
        }
    }
}