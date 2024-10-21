package com.example.planup.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
    val scrollState = rememberScrollState()

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
    val backgroundSize = 200.dp

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0XFF181A20))
                //.verticalScroll(scrollState),
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
                        Log.d("ProjectDetailScreen", "projectId: $projectId" + " Tipo: ${projectId::class.simpleName}")
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

            /*Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Card(
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .padding(20.dp, 0.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = CardDefaults.shape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0XFF1F222A)
                    )
                ) {
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))

                        Text(text = "Pendentes", style = MaterialTheme.typography.titleSmall, color = Color.White)

                        Spacer(modifier = Modifier.width(5.dp))

                        Box {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize(Alignment.Center)
                                    .background(
                                        Color.Transparent,
                                    )
                                    .border(
                                        1.dp,
                                        Color(0XFF246BFD),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                            ) {
                                Text(
                                    text = "0",
                                    modifier = Modifier.padding(12.dp, 0.dp,12.dp, 0.dp),
                                    fontSize = 10.sp,
                                    color = Color(0XFF246BFD),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier.wrapContentSize(),
                        ) {
                            IconButton(
                                onClick = {
                                    /*TODO*/
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Excluir lista",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            IconButton(
                                modifier = Modifier.padding(25.dp, 0.dp, 0.dp, 0.dp),
                                onClick = {
                                    navController.navigate("create_task_screen/$projectId") {
                                        popUpTo("project_detail_screen/$projectId") { inclusive = false }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Adicionar nova tarefa",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(0.dp, 10.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = CardDefaults.shape,
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0XFF35383f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(text = "Nome", style = MaterialTheme.typography.titleSmall, color = Color.White)

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(text = "19/09/2003", style = MaterialTheme.typography.bodyMedium, color = Color.White)

                                Spacer(modifier = Modifier.height(10.dp))
                                
                                Button(
                                    onClick = {

                                    },
                                    shape = ButtonDefaults.textShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0XFF246BFD),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(text = "Mover", style = MaterialTheme.typography.bodySmall, color = Color.White)
                                }
                            }
                        }

                        Card(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(20.dp, 10.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = CardDefaults.shape,
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0XFF35383f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(text = "Nome2", style = MaterialTheme.typography.titleSmall, color = Color.White)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "21/09/2014", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {

                                    },
                                    shape = ButtonDefaults.shape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0XFF246BFD),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(text = "Mover", style = MaterialTheme.typography.bodySmall, color = Color.White)
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .padding(20.dp, 0.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = CardDefaults.shape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0XFF1F222A)
                    )
                ) {
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))

                        Text(text = "Em andamento", style = MaterialTheme.typography.titleSmall, color = Color.White)

                        Spacer(modifier = Modifier.width(5.dp))

                        Box {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize(Alignment.Center)
                                    .background(
                                        Color.Transparent,
                                    )
                                    .border(
                                        1.dp,
                                        Color(0XFF246BFD),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                            ) {
                                Text(
                                    text = "0",
                                    modifier = Modifier.padding(12.dp, 0.dp,12.dp, 0.dp),
                                    fontSize = 10.sp,
                                    color = Color(0XFF246BFD),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier.wrapContentSize(),
                        ) {
                            IconButton(
                                onClick = {
                                    /*TODO*/
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Excluir lista",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            IconButton(
                                modifier = Modifier.padding(25.dp, 0.dp, 0.dp, 0.dp),
                                onClick = {
                                    navController.navigate("create_task_screen/$projectId") {
                                        popUpTo("project_detail_screen/$projectId") { inclusive = false }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Adicionar nova tarefa",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Card(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(20.dp, 10.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = CardDefaults.shape,
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0XFF35383f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(text = "Nome2", style = MaterialTheme.typography.titleSmall, color = Color.White)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "21/09/2014", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {

                                    },
                                    shape = ButtonDefaults.shape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0XFF246BFD),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(text = "Mover", style = MaterialTheme.typography.bodySmall, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))*/

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
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth(),
            //.horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(20.dp, 0.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = CardDefaults.shape,
            colors = CardDefaults.cardColors(
                containerColor = Color(0XFF1F222A)
            )
        ) {
            Spacer(modifier = Modifier.width(10.dp))

            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = tasks.name, style = MaterialTheme.typography.titleSmall)

                Spacer(modifier = Modifier.width(5.dp))

                Box {
                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                            .background(
                                Color.Transparent,
                            )
                            .border(1.dp, Color(0XFF246BFD), shape = RoundedCornerShape(20.dp))
                    ) {
                        Text(
                            text = tasks.tasks.size.toString(),
                            modifier = Modifier.padding(12.dp, 0.dp,12.dp, 0.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0XFF246BFD),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier.wrapContentSize(),
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate("delete_task_list_screen/$projectId") {
                                popUpTo("project_detail_screen/$projectId") { inclusive = false }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Excluir lista",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        modifier = Modifier.padding(25.dp, 0.dp, 0.dp, 0.dp),
                        onClick = {
                            navController.navigate("create_task_screen/$projectId/${tasks._id}") {
                                popUpTo("project_detail_screen/$projectId") { inclusive = false }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Adicionar nova tarefa",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                items(tasks.tasks) { task ->
                    TaskItem(
                        navController = navController,
                        task = task,
                        projectId = projectId,
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
fun TaskItem(navController: NavHostController, task: TaskPreview, projectId: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp)
                .clickable(onClick = onClick),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = CardDefaults.shape,
            colors = CardDefaults.cardColors(
                containerColor = Color(0XFF35383f)
            )
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

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        navController.navigate("move_task_screen/$projectId/${task._id}") {
                            popUpTo("project_detail_screen/$projectId") { inclusive = false }
                        }
                    },
                    shape = ButtonDefaults.shape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0XFF246BFD),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Mover", style = MaterialTheme.typography.bodySmall, color = Color.White)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}
