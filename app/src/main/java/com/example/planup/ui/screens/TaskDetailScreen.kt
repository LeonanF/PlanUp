package com.example.planup.ui.screens

import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planup.R
import com.example.planup.model.Task
import com.example.planup.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(taskId: String, navController: NavHostController) {

    val task = remember { mutableStateOf<Task?>(null) }
    val error = remember { mutableStateOf<String?>(null) }
    //val comments = remember { mutableStateListOf<CommentRequest>() } // Altera para armazenar comentários com e-mail
    val comments = remember { mutableStateListOf<Pair<String, String>>() } // sem banco de dados
    val isEditingDescription = remember { mutableStateOf(false) }
    val descriptionText = remember { mutableStateOf("") }
    val commentText = remember { mutableStateOf("") }
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email
    val currentDate = remember {
        SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date())//data do sistem
    }
    val context = LocalContext.current
    val replyText = remember { mutableStateOf("") }
    val replyingTo = remember { mutableStateOf<Pair<String, String>?>(null) } // Guarda o comentário sendo respondido

    LaunchedEffect(taskId) {
        TaskRepository().fetchTask(taskId) { result, errorMsg ->
            task.value = result
            error.value = errorMsg
            result?.let { taskData ->
                descriptionText.value = taskData.description ?: "Descrição não disponível"
                comments.clear()
                taskData.comments?.let { commentList ->
                    //comments.addAll(commentList) // Adiciona comentários se não for nulo
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    task.value?.let {
                        Text(
                            text = it.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF181A20),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = Color(0xFF181A20)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            error.value?.let {
                Text("Erro: $it", color = Color.Red)
            }

            task.value?.let {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1F222A))
                        .padding(16.dp)
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))

                        if (isEditingDescription.value) {
                            OutlinedTextField(
                                value = descriptionText.value,
                                onValueChange = { descriptionText.value = it },
                                label = { Text("Editar Descrição da Tarefa") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    focusedBorderColor = Color.White,
                                    unfocusedBorderColor = Color.Gray,
                                    cursorColor = Color.White
                                )
                            )

                            Button(
                                onClick = {
                                    isEditingDescription.value = false
                                    task.value = task.value?.copy(description = descriptionText.value)
                                },
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(top = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF246BFD),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Salvar")
                            }
                        } else {
                            Text(
                                text = descriptionText.value,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isEditingDescription.value = true }
                            )
                        }

                        Text(
                            text = "Data de criação: " + (task.value?.data ?: ""),
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 50.dp, 10.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn {
                            item {
                                Text(
                                    text = "Atributos: ",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 50.dp, 10.dp)
                                )
                            }

                            task.value?.attributes?.let { attributes ->
                                items(attributes) { attribute ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .background(Color.Gray)
                                    ) {
                                        Text(
                                            text = attribute.attributeName.toString(),
                                            fontSize = 18.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        OutlinedTextField(
                            value = commentText.value,
                            onValueChange = { newComment ->
                                if (newComment.length <= 500) { // Verifica se o novo comentário não excede 500 caracteres
                                    commentText.value = newComment
                                }
                            },
                            label = { Text("Adicionar comentário") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.Gray,
                                cursorColor = Color.White
                            )
                        )

                        Text(
                            text = "${500 - commentText.value.length} caracteres restantes", // Exibe quantos caracteres restam
                            color = if (commentText.value.length > 500) Color.Red else Color.Gray,
                            modifier = Modifier.padding(start = 16.dp)
                        )

                        Button(
                            onClick = {
                                if (commentText.value.isNotBlank()) {
                                    if (commentText.value.isNotBlank() && email != null) {
                                        comments.add(Pair(email, commentText.value))
                                        commentText.value = ""
                                    }
                                } else {
                                    // Você pode adicionar uma mensagem de aviso se o campo estiver vazio
                                    Toast.makeText(context, "Por favor, preencha o comentário.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF246BFD),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Adicionar Comentário")
                        }

                        if (comments.isNotEmpty()) {
                            Text(
                                text = "Comentários:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
                            )
                            //so pra testar sem banco
                            LazyColumn {
                                items(comments) { (email, comment) -> // Desestrutura o par para pegar o email e o comentário
                                    Column {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp)
                                                .background(
                                                    Color(0xFF2E2F33),
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .padding(12.dp)
                                                .clickable {
                                                    replyingTo.value =
                                                        Pair(email, comment)
                                                }
                                        ) {
                                            Text(
                                                text = "$email: \n $comment",
                                                fontSize = 16.sp,
                                                color = Color.White
                                            )
                                            Text(
                                                text = "$currentDate",
                                                color = Color.White,
                                                fontSize = 14.sp
                                            )
                                        }

                                        if (replyingTo.value == Pair(email, comment)) {
                                            OutlinedTextField(
                                                value = replyText.value,
                                                onValueChange = { newReply ->
                                                    replyText.value = newReply
                                                },
                                                label = { Text("Responder") },
                                                modifier = Modifier.fillMaxWidth()
                                                    .padding(16.dp),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedTextColor = Color.White,
                                                    focusedBorderColor = Color.White,
                                                    unfocusedBorderColor = Color.Gray,
                                                    cursorColor = Color.White
                                                )
                                            )

                                            Button(
                                                onClick = {
                                                    if (replyText.value.isNotBlank()) {
                                                        comments.add(
                                                            Pair(
                                                                email,
                                                                replyText.value
                                                            )
                                                        )
                                                        replyText.value = ""
                                                        replyingTo.value =
                                                            null
                                                    }
                                                },
                                                modifier = Modifier.align(Alignment.End)
                                                    .padding(16.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color(0xFF246BFD),
                                                    contentColor = Color.White
                                                )
                                            ) {
                                                Text("Enviar Resposta")
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                }
            } ?: run {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


/**LazyColumn {
items(comments) { comment ->
Row(
modifier = Modifier
.fillMaxWidth()
.padding(8.dp)
.background(Color(0xFF2E2F33), RoundedCornerShape(8.dp))
.padding(12.dp)
) {
Column {
Text(
text = comment.email,
fontSize = 16.sp,
color = Color.White,
fontWeight = FontWeight.Bold
)
Text(
text = comment.text,
fontSize = 14.sp,
color = Color.White
)
}*/