package com.example.planup.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.planup.repository.TaskRepository
import kotlinx.coroutines.launch

@Composable
fun DeleteModalBottomSheet(
    projectId: String,
    listId: String,
    taskId: String,
    onDismiss: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    val deleteSuccess by remember { mutableStateOf<Boolean?>(null) }
    val taskRepository = TaskRepository()
    val scope = rememberCoroutineScope()

    // Exibição do modal de alerta
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Deseja realmente deletar?", color = Color.Red)
        },
        confirmButton = {
            Button(
                onClick = {
                    isLoading = true
                    scope.launch {
                        taskRepository.deleteTask(projectId, listId, taskId)
                        isLoading = false
                        onDismiss()
                    }
                },
                enabled = !isLoading
            ) {
                Text(text = "Excluir", color = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cancelar", color = Color.White)
            }
        },
        text = {
            if (deleteSuccess == false) {
                Text(text = "Erro ao excluir a tarefa", color = Color.Red)
            }
        }
    )
}
