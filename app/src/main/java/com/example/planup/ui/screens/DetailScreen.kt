package com.example.planup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planup.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    task: Task,
    onBackClick: () -> Unit,
    onSaveChanges: (Task) -> Unit
) {
    var taskName by remember { mutableStateOf(task.name) }
    var taskDescription by remember { mutableStateOf(task.description) }
    var taskResponsible by remember { mutableStateOf("Responsável Placeholder") }
    var taskStatus by remember { mutableStateOf("Em Progresso") }
    var taskPriority by remember { mutableStateOf("Alta") }
    var taskTags by remember { mutableStateOf(listOf("Importante", "Urgente")) }
    var taskSubtasks by remember { mutableStateOf(listOf("Subtarefa 1", "Subtarefa 2")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da Tarefa") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            TextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Nome da Tarefa") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("Descrição da Tarefa") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Responsável: $taskResponsible", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Status: $taskStatus", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Prioridade: $taskPriority", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Etiquetas: ${taskTags.joinToString()}", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Subtarefas:", fontSize = 16.sp)
            taskSubtasks.forEach { subtask ->
                Text("- $subtask", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // Mantém o id original ao salvar as alterações
                val updatedTask = task.copy(
                    name = taskName,
                    description = taskDescription,
                    id = task.id // Preserva o ID ao atualizar os outros campos
                )
                onSaveChanges(updatedTask)
            }) {
                Text("Salvar Alterações")
            }
        }
    }
}
