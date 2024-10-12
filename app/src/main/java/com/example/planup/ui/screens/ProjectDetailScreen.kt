package com.example.planup.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planup.model.TaskPreview
import com.example.planup.repository.TaskRepository
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    navController: NavHostController,
    projectId: String,
    projectName: String,
    onTaskClick: (String) -> Unit
) {
    val taskPreview = remember { mutableStateOf<List<TaskPreview>?>(null) }
    val error = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(projectId) {
        TaskRepository().fetchTaskPreviews(projectId) { result, errorMsg ->
            taskPreview.value = result
            error.value = errorMsg
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = projectName, style = MaterialTheme.typography.titleSmall) })
        }
    ) { innerPadding ->

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.navigate("create_task_screen/$projectId") {
                        popUpTo("project_detail_screen/$projectId/$projectName") { inclusive = false }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Adicionar nova tarefa",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        taskPreview.value?.let { taskPreviewList ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(taskPreviewList) { task ->
                    TaskItem(
                        task = task,
                        onClick = {
                            task._id?.let { taskId ->
                                onTaskClick(taskId)
                            }
                        }
                    )
                }
            }
        }


    }
}

@Composable
fun TaskItem(task: TaskPreview, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            task.name?.let { name ->
                Text(text = name, style = MaterialTheme.typography.titleSmall)
            }
            Spacer(modifier = Modifier.height(10.dp))
            task.data?.let { date ->
                Text(text = "Data: $date")
            }
        }
    }
}