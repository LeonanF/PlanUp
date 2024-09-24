package com.example.planup.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import android.util.Log
import com.example.planup.network.RetrofitInstance.apiService
import com.example.planup.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun DeleteModalBottomSheet(
    taskId: String,  // Recebe o ID da tarefa a ser deletada
    onDismiss: () -> Unit  // Função para fechar o modal
) {
    var isLoading by remember { mutableStateOf(false) }  // Estado para controlar o carregamento
    var deleteSuccess by remember { mutableStateOf<Boolean?>(null) }  // Estado para verificar se a exclusão foi bem-sucedida ou não
    val taskRepository = TaskRepository()  // Instância do TaskRepository para chamar a função deleteTask
    val scope = rememberCoroutineScope()  // Coroutines para operações assíncronas

    // Exibição do modal de alerta
    AlertDialog(
        onDismissRequest = onDismiss,  // Fecha o modal quando clicado fora ou cancelado
        title = {
            Text(text = "Deseja realmente deletar?", color = Color.Red)
        },
        confirmButton = {
            // Botão de exclusão
            Button(
                onClick = {
                    isLoading = true  // Marca como carregando
                    scope.launch {
                        // Chamando a função deleteTask para excluir a tarefa
                        taskRepository.deleteTask(taskId)
                        isLoading = false  // Conclui o carregamento
                        onDismiss()  // Fecha o modal após a tentativa de exclusão
                    }
                },
                enabled = !isLoading  // Desabilita o botão enquanto estiver carregando
            ) {
                Text(text = "Excluir", color = Color.White)
            }
        },
        dismissButton = {
            // Botão de cancelar
            Button(onClick = onDismiss) {
                Text(text = "Cancelar", color = Color.White)
            }
        },
        text = {
            // Mensagem de erro caso a exclusão falhe
            if (deleteSuccess == false) {
                Text(text = "Erro ao excluir a tarefa", color = Color.Red)
            }
        }
    )
}
