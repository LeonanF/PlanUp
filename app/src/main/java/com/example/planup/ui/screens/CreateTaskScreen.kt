package com.example.planup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planup.model.Task
import com.example.planup.ui.theme.Purple40
import com.example.planup.ui.theme.PurpleGrey80
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTask(navController: NavHostController, innerPadding: PaddingValues) {
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple40)
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey80)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            var title by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var subtasks by remember { mutableStateOf(listOf<String>()) }
            var newSubtask by remember { mutableStateOf("") }
            var completedSubtasks by remember { mutableStateOf(mutableMapOf<Int, Boolean>()) }
            val isTaskCompleted by remember {
                derivedStateOf { completedSubtasks.values.all { it } && subtasks.isNotEmpty() }
            }
            var id by remember { mutableStateOf(UUID.randomUUID().toString()) }

            // Campo de entrada para o título
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
            )

            // Campo de entrada para a descrição
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                singleLine = false,
                maxLines = 5
            )

            // Adicionar subtarefas
            OutlinedTextField(
                value = newSubtask,
                onValueChange = { newSubtask = it },
                label = { Text("Adicionar Subtarefa") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                singleLine = true
            )

            Button(
                onClick = {
                    if (newSubtask.isNotEmpty()) {
                        subtasks = subtasks + newSubtask
                        completedSubtasks[subtasks.size] = false // Adiciona uma nova subtarefa não concluída
                        newSubtask = "" // Limpa o campo de entrada
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Adicionar Subtarefa")
            }

            // Exibição das subtarefas
            subtasks.forEachIndexed { index, subtask ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = completedSubtasks[index] == true,
                        onCheckedChange = { isChecked ->
                            completedSubtasks[index] = isChecked
                        }
                    )
                    BasicTextField(
                        value = subtask,
                        onValueChange = { updatedText ->
                            subtasks = subtasks.toMutableList().apply { set(index, updatedText) }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Verificação automática da conclusão da tarefa principal
            Text(
                text = if (isTaskCompleted) "Tarefa Principal Concluída!" else "Tarefa em andamento",
                fontSize = 16.sp,
                color = if (isTaskCompleted) Color.Green else Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão de criar
            Button(
                onClick = {
                    // Lógica de adicionar a tarefa com suas subtarefas
                    val newTask = Task(name = title, description = description, id = id)
                    //.addTask(newTask)  // Lógica para salvar a tarefa no repositório
                    navController.popBackStack() // Voltar para a lista de tarefas após criar
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = Color.White
                ),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Criar Tarefa")
            }
        }
    }
}
