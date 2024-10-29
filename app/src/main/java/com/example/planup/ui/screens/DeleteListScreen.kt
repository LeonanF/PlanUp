package com.example.planup.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planup.repository.TaskListRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteListScreen(
    listId: String,
    projectId: String,
    onDismiss: () -> Unit
) {
    val taskListRepository = TaskListRepository()
    val context = LocalContext.current

    ModalBottomSheet(
        modifier = Modifier
            .heightIn(200.dp),
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF181A20) // Fundo cinza escuro
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Deletar Projeto",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0XFFF75555) // Vermelho
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Você tem certeza que quer deletar o projeto?",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    taskListRepository.deleteList(listId) { result ->
                        if (result) {
                            Toast.makeText(context, "Lista deletada com sucesso", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Erro ao deletar a lista", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF246BFD),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(55.dp)
            ) {
                Text(
                    "Sim, Deletar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF35383F),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(55.dp)
            ) {
                Text(
                    "Cancelar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
