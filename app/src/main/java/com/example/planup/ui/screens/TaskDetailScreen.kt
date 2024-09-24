package com.example.planup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planup.R
import com.example.planup.model.Project
import com.example.planup.model.Task
import com.example.planup.repository.ProjectRepository
import com.example.planup.repository.TaskRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(taskId: String, navController: NavHostController) {
    val task = remember { mutableStateOf<Task?>(null) }
    val error = remember { mutableStateOf<String?>(null) }
    val comments = remember { mutableStateListOf<String>() }
    val isEditingDescription = remember { mutableStateOf(false) }
    val descriptionText = remember { mutableStateOf(task.value!!.description ?: "") }
    val commentText = remember { mutableStateOf("") }

    LaunchedEffect(taskId) {
        TaskRepository().fetchTask(taskId) { result, errorMsg ->
            task.value = result
            error.value = errorMsg
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = task.value!!.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF181A20),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = Color(0xFF181A20)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            error.value?.let {
                Text("Erro: $it", color = Color.Red)
            }

            task.value?.let {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1F222A))
                        .padding(16.dp)
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))

                        if (isEditingDescription.value) {
                            OutlinedTextField(
                                value = descriptionText.value,
                                onValueChange = { descriptionText.value = it },
                                label = { Text("Editar Descrição da Tarefa") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    focusedBorderColor = Color.White,
                                    unfocusedBorderColor = Color.Gray,
                                    cursorColor = Color.White
                                )
                            )

                            Button(
                                onClick = {
                                    isEditingDescription.value = false
                                    task.value!!.description = descriptionText.value
                                },
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(top = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF246BFD),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Salvar")
                            }
                        } else {
                            Text(
                                text = descriptionText.value,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isEditingDescription.value = true }
                            )
                        }

                        Text(
                            text = "Data de criação: " +task.value!!.data,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 50.dp, 10.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn {
                            item {
                                Text(
                                    text = "Atributos: ",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 50.dp, 10.dp)
                                )
                            }

                            items(task.value!!.attributes) { attribute ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .background(Color.Gray)
                                ) {
                                    Text(
                                        text = attribute.attributeName.toString(),
                                        fontSize = 18.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        OutlinedTextField(
                            value = commentText.value,
                            onValueChange = { commentText.value = it },
                            label = { Text("Adicionar comentário") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.Gray,
                                cursorColor = Color.White
                            )
                        )

                        Button(
                            onClick = {
                                if (commentText.value.isNotBlank()) {
                                    comments.add(commentText.value)
                                    commentText.value = ""
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF246BFD),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Adicionar Comentário")
                        }

                        if (comments.isNotEmpty()) {
                            Text(
                                text = "Comentários:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
                            )

                            LazyColumn {
                                items(comments) { comment ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .background(Color(0xFF2E2F33), RoundedCornerShape(8.dp))
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = comment,
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            } ?: run {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}