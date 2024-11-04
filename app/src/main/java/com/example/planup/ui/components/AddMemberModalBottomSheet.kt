package com.example.planup.ui.components

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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planup.model.MemberRequest
import com.example.planup.repository.ProjectRepository
import com.example.planup.repository.UserRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberModalBottomSheet(
    projectId: String,
    onDismiss: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        modifier = Modifier.heightIn(450.dp),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Novo membro",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
            )

            HorizontalDivider(color = Color(0xFF35383F), thickness = 2.dp)

            OutlinedTextField(
                value = email,
                onValueChange = { newText -> email = newText },
                label = { Text("Email do membro", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(0.dp, 10.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF35383F),
                    focusedContainerColor = Color(0xFF1F222A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(25.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        UserRepository().fetchUserByEmail(email) { user ->
                            if (user != null) {
                                ProjectRepository().postMember(
                                    MemberRequest(projectId = projectId, memberId = user.id)
                                ) { success ->
                                    if (success) {
                                        onDismiss()
                                    } else {
                                        errorMessage = "Erro ao adicionar o membro. Tente novamente."
                                    }
                                }
                            } else {
                                errorMessage = "Usuário não encontrado."
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF246BFD),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .heightIn(65.dp)
            ) {
                Text("Adicionar membro", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
