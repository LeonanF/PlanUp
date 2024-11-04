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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.planup.R
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
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        ProjectRepository().fetchMembers(projectId) { fetchedMemberIds, errorMsg ->
            if (fetchedMemberIds != null) {
                error = errorMsg
                fetchedMemberIds.forEach { memberId ->
                    UserRepository().fetchUserById(memberId) { user ->
                        user?.let {
                            members = members + it.nome
                        }
                    }
                }
            } else {
                error = errorMsg
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss
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
                modifier = Modifier.padding(bottom = 20.dp)
            )

            if (error != null) {
                Text(
                    text = "Erro: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(members) { member ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .height(20.dp)
                            .clickable {
                                selectedMemberId = member
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.person),
                            contentDescription = "MembroIcon",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(end = 5.dp)
                        )

                        Text(text = member)
                        Spacer(modifier = Modifier.weight(2f))

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

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
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

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        selectedMemberId?.let {
                            TaskRepository().removeMemberTask(projectId, listId, taskId, it)
                            selectedMemberId = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .heightIn(50.dp)
                        .padding(10.dp)
                ) {
                    Text("Remover Responsável")
                }
            }
        }
    }
}

