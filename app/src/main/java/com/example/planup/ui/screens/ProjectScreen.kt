package com.example.planup.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.planup.model.Project
import com.example.planup.model.ProjectViewModel
import com.example.planup.model.Task
import com.google.gson.Gson

@Composable
fun ProjectScreen(navController: NavHostController, project: Project) {
    val viewModel: ProjectViewModel = viewModel()
    val backgroundImage: Painter = painterResource(viewModel.getImageForElement(project.name))
    val backgroundSize = 200.dp
    val color = viewModel.getColorForElement(project.name)

    var progress by remember { mutableFloatStateOf(0.0f) }
    var numTarefas by remember { mutableIntStateOf(0) }
    var tarefasConcluidas by remember { mutableIntStateOf(0) }
    var check by remember { mutableStateOf(false) }
    //val tasks = remember { mutableStateOf(project.taskLists) }
    val tasks = remember { mutableStateOf<List<Task>?>(null) }

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
                Image(
                    painter = backgroundImage,
                    contentDescription = "Imagem de fundo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(backgroundSize),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 0.dp, 20.dp, 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        /*IconButton(
                            onClick = {
                                /*TODO*/
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Pesquisar",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                /*TODO*/
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = "Outros",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }*/
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = project.name,
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 0.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            project.description?.let {
                Text(
                    text = project.description,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.LightGray,
                    modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 0.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp, 0.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.owner,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                )

                IconButton(
                    onClick = {
                        val projectJson = Gson().toJson(project)
                        navController.navigate("create_task_screen/{projectId}") {
                            popUpTo("project_screen/$projectJson"){ inclusive = false }
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

            Box(modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 0.dp)) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(
                            Color(color.value),
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Text(
                        text = "$tarefasConcluidas / $numTarefas",
                        modifier = Modifier.padding(15.dp, 2.dp,15.dp, 2.dp),
                        fontSize = 14.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp, 20.dp, 0.dp),
                progress = { progress },
                color = Color(color.value),
                trackColor = Color(0XFF35383F),
            )

            Spacer(modifier = Modifier.height(20.dp))

            tasks.value?.let { taskList ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(taskList) { task ->
                        Box(
                            modifier = Modifier
                                .padding(20.dp, 10.dp)
                        ) {
                            Button(
                                modifier = Modifier
                                    .heightIn(200.dp)
                                    .fillMaxWidth(),
                                onClick = {
                                    val taskId = task._id
                                    val projectJson = Gson().toJson(project)
                                    navController.navigate("task_detail_screen/$taskId") {
                                        popUpTo("project_screen/$projectJson"){ inclusive = false }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0XFF35383F),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(text = task.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(text = task.description, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.LightGray)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(text = task.data, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.LightGray)
                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Tarefa concluída", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.LightGray)
                                        Spacer(modifier = Modifier.height(10.dp))

                                        Checkbox(
                                            checked = check,
                                            onCheckedChange ={
                                                check = it
                                            },
                                            modifier = Modifier.size(16.dp),
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = Color(0XFF246BFD),
                                                uncheckedColor = Color(0XFF35383F)
                                            )
                                        )
                                    }
                                }
                            }
                            numTarefas = taskList.size
                            if(check) tarefasConcluidas++ else if(tarefasConcluidas > 0) tarefasConcluidas-- else tarefasConcluidas = 0
                            progress = tarefasConcluidas.toFloat() / numTarefas.toFloat()
                        }
                    }
                }
            } ?: run {
                Scaffold(
                    containerColor = Color(0xFF181A20)
                ) { padding ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator(modifier = Modifier.padding(padding))
                    }
                }
            }
        }
    }
}