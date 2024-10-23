package com.example.planup.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.planup.model.ProjectDetailPreview
import com.example.planup.model.ProjectViewModel
import com.example.planup.repository.ProjectRepository

@Composable
fun ProjectDetailScreen(
    navController: NavHostController,
    projectId: String
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
    val backgroundSize = 200.dp

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0XFF181A20))
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
                        modifier = Modifier
                            .fillMaxWidth()
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
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        items(taskLists) { taskList ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp, 0.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                shape = CardDefaults.shape,
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0XFF1F222A)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp, 0.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = taskList.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color.White,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                    )

                                    Spacer(modifier = Modifier.width(5.dp))

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
                                            text = taskList.tasks.size.toString(),
                                            modifier = Modifier.padding(12.dp, 0.dp,12.dp, 0.dp),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0XFF246BFD),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(45.dp))

                                    IconButton(
                                        onClick = {
                                            navController.navigate("delete_list_screen/${taskList._id}/$projectId") {
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
                                        onClick = {
                                            navController.navigate("create_task_screen/$projectId/${taskList._id}") {
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

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    items(taskList.tasks) { task ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable(
                                                    onClick = {
                                                        task._id?.let { taskId ->
                                                            navController.navigate("task_detail_screen/$taskId/$projectId/${taskList._id}")
                                                        }
                                                    }
                                                ),
                                            elevation = CardDefaults.cardElevation(4.dp),
                                            shape = CardDefaults.shape,
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0XFF35383f)
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier.fillMaxWidth()
                                                    .padding(16.dp),
                                                horizontalAlignment = Alignment.Start
                                            ) {
                                                task.name?.let { name ->
                                                    Text(
                                                        text = name,
                                                        style = MaterialTheme.typography.titleSmall,
                                                        maxLines = 5,
                                                        overflow = TextOverflow.Ellipsis,
                                                        modifier = Modifier.width(160.dp),
                                                    )
                                                }

                                                Spacer(modifier = Modifier.height(10.dp))

                                                task.data?.let { date ->
                                                    Text(text = "Data: $date")
                                                }

                                                Spacer(modifier = Modifier.height(10.dp))

                                                Box(
                                                    modifier = Modifier
                                                        .wrapContentSize(Alignment.CenterStart)
                                                        .align(Alignment.Start)
                                                ) {
                                                    Row(
                                                        modifier = Modifier.wrapContentSize(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Button(
                                                            onClick = {
                                                                navController.navigate("move_task_screen/$projectId/${task._id}/${taskList._id}") {
                                                                    popUpTo("project_detail_screen/$projectId") { inclusive = false }
                                                                }
                                                            },
                                                            shape = ButtonDefaults.textShape,
                                                            colors = ButtonDefaults.buttonColors(
                                                                containerColor = Color(0XFF246BFD),
                                                                contentColor = Color.White
                                                            ),
                                                            modifier = Modifier.heightIn(30.dp)
                                                        ) {
                                                            Text(text = "Mover", fontSize = 10.sp, color = Color.White)
                                                        }

                                                        Spacer(modifier = Modifier.width(5.dp))

                                                        Button(
                                                            onClick = {
                                                                navController.navigate("delete_modal_bottom_sheet/$projectId/${taskList._id}/${task._id}") {
                                                                    popUpTo("project_detail_screen/$projectId") { inclusive = false }
                                                                }
                                                            },
                                                            shape = ButtonDefaults.textShape,
                                                            colors = ButtonDefaults.buttonColors(
                                                                containerColor = Color(0XFFF75555),
                                                                contentColor = Color.White
                                                            ),
                                                            modifier = Modifier.heightIn(30.dp)
                                                        ) {
                                                            Text(text = "Excluir", fontSize = 10.sp, color = Color.White)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}