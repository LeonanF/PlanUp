package com.example.planup.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.planup.repository.ProjectRepository
import com.example.planup.repository.TaskRepository
import com.example.planup.repository.UserRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberSelectionModal(
    projectId: String,
    listId: String,
    taskId: String,
    onMemberSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var members by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedMemberId by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
         ProjectRepository().fetchMembers(projectId) { fetchedMembers, error ->
            members = fetchedMembers ?: emptyList()
            errorMessage = error
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Selecionar Responsável pela Tarefa",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (errorMessage != null) {
                Text(
                    text = "Erro: $errorMessage",
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(members) { member ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedMemberId = member
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = member)
                        Spacer(modifier = Modifier.weight(1f))

                        if (selectedMemberId == member) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selecionado",
                                tint = Color.Green
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    selectedMemberId?.let {
                        TaskRepository().postMemberTask(projectId, listId, taskId, it)
                        onMemberSelected(it)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF246BFD),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .heightIn(50.dp)
            ) {
                Text("Confirmar Seleção")
            }
        }
    }
}
