package com.example.planup.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.planup.model.Project
import com.example.planup.repository.ProjectRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson


@Composable
fun ProjectListScreen(navController: NavHostController) {

    val projects = remember { mutableStateOf<List<Project>?>(null) }
    val error = remember { mutableStateOf<String?>(null) }

    val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

    LaunchedEffect(userId) {
        ProjectRepository().fetchUserProjects(userId) { result, errorMsg ->
            projects.value = result
            error.value = errorMsg
        }
    }

    Scaffold {
        innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
        ) {
            error.value?.let {
                Text("Erro: $it", color = Color.Red)
            }

            projects.value?.let { projectList ->
                LazyColumn (modifier = Modifier
                    .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    items(projectList) { project ->
                        Box(modifier = Modifier
                            .fillMaxWidth(0.8f)
                            ){
                            Button(onClick = {
                                val projectJson = Gson().toJson(project)
                                navController.navigate("project_screen/$projectJson"){
                                    popUpTo("project_list_screen"){ inclusive = false }
                                }
                                             }, modifier = Modifier
                                .fillMaxWidth()){
                                Text(project.name)
                            }
                        }
                    }
                }
            } ?: run {
                CircularProgressIndicator()
            }
        }
    }
}