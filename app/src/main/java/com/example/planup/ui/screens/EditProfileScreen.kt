package com.example.planup.ui.screens

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.EmailAuthProvider
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.planup.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val context = LocalContext.current
    val newEmail = remember { mutableStateOf("") }
    val currentPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar Conta",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Voltar",
                            tint = Color.Gray
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Email Atual: ${currentUser?.email ?: "Usuário não autenticado"}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextField(
                value = newEmail.value,
                onValueChange = { newEmail.value = it },
                label = { Text("Novo Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = currentPassword.value,
                onValueChange = { currentPassword.value = it },
                label = { Text("Senha Atual") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = newPassword.value,
                onValueChange = { newPassword.value = it },
                label = { Text("Nova Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (currentUser != null) {
                        val credential = EmailAuthProvider.getCredential(currentUser.email!!, currentPassword.value)

                        currentUser.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                            if (reauthTask.isSuccessful) {
                                if (newEmail.value.isNotBlank()) {
                                    currentUser.updateEmail(newEmail.value).addOnCompleteListener { emailTask ->
                                        if (emailTask.isSuccessful) {
                                            Toast.makeText(context, "Email atualizado com sucesso", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Falha ao atualizar email: ${emailTask.exception?.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }

                                if (newPassword.value.isNotBlank()) {
                                    currentUser.updatePassword(newPassword.value).addOnCompleteListener { passwordTask ->
                                        if (passwordTask.isSuccessful) {
                                            Toast.makeText(context, "Senha atualizada com sucesso", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Falha ao atualizar senha: ${passwordTask.exception?.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Reautenticação falhou: ${reauthTask.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Usuário não autenticado", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF246BFD))
            ) {
                Text("Salvar Alterações", color = Color.White)
            }
        }
    }
}
