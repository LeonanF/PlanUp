package com.example.planup.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.planup.model.ProjectDetailPreview
import com.example.planup.model.ProjectViewModel
import com.example.planup.model.TaskListPreview
import com.example.planup.model.TaskPreview
import com.example.planup.repository.ProjectRepository

@Composable
fun ProjectDetailScreen(
    navController: NavHostController,
    projectId: String,
    onTaskClick: (String) -> Unit
) {
    val project = remember { mutableStateOf<ProjectDetailPreview?>(null) }
    val error = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(projectId) {
        ProjectRepository().fetchProjectPreview(projectId) { result, errorMsg ->
            project.value = result
            error.value = errorMsg
        }
    }

    val viewModel: ProjectViewModel = viewModel()
    val backgroundImage: Painter? = project.value?.name?.let {
        viewModel.getImageForElement(it)
    }?.let { painterResource(it) }
    val backgroundSize = 300.dp

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0XFF181A20)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(backgroundSize)
                    .background(Color.Gray)
            ) {
                if (backgroundImage != null) {
                    Image(
                        painter = backgroundImage,
                        contentDescription = "Imagem de fundo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(backgroundSize),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 0.dp, 20.dp, 0.dp)
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            project.value?.name?.let {
                Text(
                    text = it,
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 0.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            project.value?.description?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.LightGray,
                    modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 0.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp, 0.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                project.value?.owner?.let {
                    Text(
                        text = it,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                }

                IconButton(
                    onClick = {
                        Log.d("ProjectDetailScreen", "projectId: $projectId")
                        navController.navigate("create_task_list/$projectId") {
                            popUpTo("project_detail_screen/$projectId") { inclusive = false }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Adicionar nova lista de tarefas",
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            error.value?.let {
                Text(
                    text = "Erro: $it",
                    color = Color.Red
                )
            }

            project.value?.taskLists?.let { taskLists ->
                if (taskLists.isEmpty()) {

                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhuma lista foi criada",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyRow(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        items(taskLists) { taskList ->
                            TaskListItem(
                                tasks = taskList,
                                navController = navController,
                                projectId = projectId,
                                onTaskClick = onTaskClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskListItem(
    tasks: TaskListPreview,
    navController: NavHostController,
    projectId: String,
    onTaskClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(8.dp)
            .background(Color(0XFF1F222A)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = tasks.name, style = MaterialTheme.typography.titleSmall)

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = tasks.tasks.size.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Blue
            )

            IconButton(
                onClick = {
                    navController.navigate("create_task_screen/$projectId") {
                        popUpTo("project_detail_screen/$projectId") { inclusive = false }
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

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0XFF35383f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(tasks.tasks) { task ->
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
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
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
