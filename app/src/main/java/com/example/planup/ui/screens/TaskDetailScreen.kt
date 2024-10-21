package com.example.planup.ui.screens

import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
fun TaskDetailScreen(taskId: String, listId: String, projectId: String, navController: NavHostController) {

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
    var showReplies by remember { mutableStateOf(false) } // Estado para controlar a visibilidade das respostas
    val replies = mutableListOf<Pair<String, String>>() // Definindo a lista de respostas
    var isReplyFieldVisible by remember { mutableStateOf(false) } // Variável para controlar a visibilidade do campo de resposta


    LaunchedEffect(taskId) {
        TaskRepository().fetchTask(taskId, listId, projectId) { result, errorMsg ->
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
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            item {
                error.value?.let {
                    Text("Erro: $it", color = Color.Red)
                }
            }

            item {
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

                            // Campo de edição da descrição
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
                                        task.value =
                                            task.value?.copy(description = descriptionText.value)
                                        // Chamar função para atualizar o banco de dados

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

                            // Exibição da data de criação
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

                            // Atributos
                            Text(
                                text = "Atributos: ",
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(0.dp, 50.dp, 10.dp)
                            )
                            // Exibição e edição dos atributos
                            task.value?.attributes?.let { attributes ->
                                attributes.forEach { attribute ->
                                    // Inicializa o status selecionado com o nome do atributo
                                    var selectedStatus by remember {
                                        mutableStateOf(
                                            attribute.attributeName ?: "Atributo sem nome"
                                        )
                                    }
                                    var expanded by remember { mutableStateOf(false) }

                                    Column(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .background(Color.Gray)
                                            .fillMaxWidth()
                                    ) {
                                        // Nome do atributo
                                        Text(
                                            text = attribute.attributeName ?: "Atributo sem nome",
                                            fontSize = 18.sp,
                                            color = Color.White
                                        )

                                        // Exibir o status selecionado e o botão de edição
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = selectedStatus,
                                                fontSize = 16.sp,
                                                color = Color.LightGray,
                                                modifier = Modifier.padding(end = 8.dp) // Espaçamento entre o texto e o botão
                                            )

                                            IconButton(onClick = { expanded = true }) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Editar",
                                                    tint = Color.White
                                                )
                                            }

                                            DropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = { expanded = false }
                                            ) {
                                                DropdownMenuItem(
                                                    text = { Text("Não Iniciado") },
                                                    onClick = {
                                                        selectedStatus = "Não Iniciado"
                                                        expanded = false
                                                    }
                                                )
                                                DropdownMenuItem(
                                                    text = { Text("Em Andamento") },
                                                    onClick = {
                                                        selectedStatus = "Em Andamento"
                                                        expanded = false
                                                    }
                                                )
                                                DropdownMenuItem(
                                                    text = { Text("Concluída") },
                                                    onClick = {
                                                        selectedStatus = "Concluída"
                                                        expanded = false
                                                    }
                                                )
                                            }

                                            // Botão para salvar a alteração do estado
                                            Button(
                                                onClick = {
                                                    // Chamar função para atualizar o atributo no banco de dados
                                                    // Exemplo: updateAttribute(attribute.id, selectedStatus)
                                                },
                                                modifier = Modifier.padding(start = 8.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color(0xFF246BFD),
                                                    contentColor = Color.White
                                                )
                                            ) {
                                                Text("Salvar")
                                            }
                                        }
                                    }
                                }
                            } ?: run {
                                Text(
                                    text = "Nenhum atributo disponível.",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))

                OutlinedTextField(
                    value = commentText.value,
                    onValueChange = { newComment ->
                        if (newComment.length <= 500) {
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
                    text = "${500 - commentText.value.length} caracteres restantes",
                    color = if (commentText.value.length > 500) Color.Red else Color.Gray,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            if (commentText.value.isNotBlank() && email != null) {
                                comments.add(Pair(email, commentText.value))
                                commentText.value = ""
                            } else {
                                Toast.makeText(
                                    context,
                                    "Por favor, preencha o comentário.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF246BFD),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Adicionar Comentário")
                    }
                }
            }

            item {
                if (comments.isNotEmpty()) {
                    Text(
                        text = "Comentários:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )

                    comments.forEach { (email, comment) ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(Color(0xFF2E2F33), RoundedCornerShape(8.dp))
                                .padding(16.dp)
                                .clickable {
                                    replyingTo.value = Pair(email, comment)
                                }
                        ) {
                            // Exibindo o email e o comentário original
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = email,
                                        fontSize = 16.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = comment,
                                        fontSize = 14.sp,
                                        color = Color.LightGray
                                    )
                                }

                                Text(
                                    text = currentDate,
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }

                            // Alinhamento dos botões e exibição do campo de resposta
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                // Botão para exibir/ocultar respostas à direita
                                IconButton(onClick = { showReplies = !showReplies }) {
                                    Icon(
                                        painter = painterResource(id = if (showReplies) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
                                        contentDescription = if (showReplies) "Ocultar Respostas" else "Ver Respostas",
                                        tint = Color.White
                                    )
                                }

                                // Botão "Responder" para abrir/fechar o campo de resposta
                                Button(
                                    onClick = {
                                        isReplyFieldVisible =
                                            !isReplyFieldVisible // Alterna a visibilidade do campo de resposta
                                        if (isReplyFieldVisible) {
                                            replyText.value =
                                                "" // Limpa o campo de texto quando o campo é aberto
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF246BFD),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Responder")
                                }
                            }

                            // Campo de resposta
                            if (isReplyFieldVisible) {
                                OutlinedTextField(
                                    value = replyText.value,
                                    onValueChange = { newReply -> replyText.value = newReply },
                                    label = { Text("Responder") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        focusedBorderColor = Color.White,
                                        unfocusedBorderColor = Color.Gray,
                                        cursorColor = Color.White
                                    )
                                )

                                Button(
                                    onClick = {
                                        if (replyText.value.isNotBlank() && email != null) {
                                            // Adiciona a resposta à lista de respostas como um par (email, texto da resposta)
                                            replies.add(Pair(email, replyText.value))
                                            replyText.value = "" // Limpa o campo de texto após enviar
                                            isReplyFieldVisible = false // Fecha o campo de resposta após enviar
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Por favor, preencha a resposta.",
                                                Toast.LENGTH_SHORT
                                            ).show() // Exibe uma mensagem de erro
                                        }
                                    },
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(top = 8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF246BFD),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Enviar Resposta")
                                }
                            }

                            // Exibir as respostas se showReplies for verdadeiro
                            if (showReplies) {
                                replies.forEach { (email, replyText) ->
                                    Column(modifier = Modifier.padding(start = 16.dp, top = 4.dp)) {
                                        Text(
                                            text = "$email respondeu:",
                                            fontSize = 16.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = replyText,
                                            fontSize = 14.sp,
                                            color = Color.LightGray
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End // Alinha o conteúdo à direita
                                        ) {
                                            Text(
                                                text = currentDate,
                                                color = Color.Gray,
                                                fontSize = 12.sp,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}