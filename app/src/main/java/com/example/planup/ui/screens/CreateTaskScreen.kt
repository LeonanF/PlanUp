package com.example.planup.ui.screens

import android.adservices.adid.AdId
import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planup.R
import com.example.planup.model.Task
import com.example.planup.repository.TaskRepository
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(navController: NavHostController, projectId: String, listId: String) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Criar Tarefa",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF181A20))
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF181A20))
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            var title by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = null,
                placeholder = {
                    Text(
                        text = "Título",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 22.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1F222A),
                    unfocusedContainerColor = Color(0xFF1F222A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = null,
                placeholder = {
                    Text(
                        text = "Descrição",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 22.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1F222A),
                    unfocusedContainerColor = Color(0xFF1F222A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val currentDate = remember {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            }

            Text(
                text = "Data de Criação: $currentDate",
                color = Color.White,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        val newTask = Task(
                            _id = null,
                            name = title,
                            description = description,
                            data = currentDate,
                            attributes = emptyList(),
                            comments = null
                        )
                        TaskRepository().postTasks(newTask)
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (title.isNotBlank() && description.isNotBlank()) Color(
                        0xFF246BFD
                    ) else Color(0xFF476EBE),
                    contentColor = Color.White
                ),
                enabled = true,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Criar Tarefa")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        // Duplicar tarefa, garantindo que o ID seja novo ou nulo
                        val duplicatedTask = Task(
                            _id = null, // Gerar novo ID ao salvar
                            name = "$title (Cópia)", // Opcional: indicar que é uma cópia
                            description = description,
                            data = currentDate,
                            attributes = emptyList(),
                            comments = null
                        )
                        TaskRepository().postTasks(duplicatedTask)
                        navController.popBackStack()
                        Toast.makeText(context, "Tarefa duplicada com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (title.isNotBlank() && description.isNotBlank()) Color(
                        0xFF246BFD
                    ) else Color(0xFF476EBE),
                    contentColor = Color.White
                ),
                enabled = true,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Duplicar Tarefa")
            }
        }
    }
}
