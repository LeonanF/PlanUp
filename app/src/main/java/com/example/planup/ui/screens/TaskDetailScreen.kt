package com.example.planup.ui.screens

import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planup.R
import com.example.planup.model.Comment
import com.example.planup.model.CommentRequest
import com.example.planup.model.Priority
import com.example.planup.model.SubtaskStatus
import com.example.planup.model.Subtask
import com.example.planup.model.Task
import com.example.planup.model.TaskRequest
import com.example.planup.model.TaskStatus
import com.example.planup.repository.SubtaskRepository
import com.example.planup.repository.TaskRepository
import com.example.planup.ui.components.CreateSubtaskModalBottomSheet
import com.example.planup.ui.components.MemberSelectionModal
import com.google.firebase.auth.FirebaseAuth
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(taskId: String, listId: String, projectId: String, navController: NavHostController) {

    val task = remember { mutableStateOf<Task?>(null) }
    val error = remember { mutableStateOf<String?>(null) }
    val comments = remember { mutableStateListOf<CommentRequest>() }
    val isEditingDescription = remember { mutableStateOf(false) }
    val descriptionText = remember { mutableStateOf("") }
    val isEditingName = remember { mutableStateOf(false) }
    val taskName = remember { mutableStateOf("") }
    val commentText = remember { mutableStateOf("") }
    var taskStatus by remember {
        mutableStateOf("")
    }
    var taskPriority by remember{
        mutableStateOf("")
    }
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email
    val currentDate = remember {
        SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date())
    }

    val context = LocalContext.current
    val replyText = remember { mutableStateOf("") }
    val replyingTo = remember { mutableStateOf<Pair<String, String>?>(null) }
    var showReplies by remember { mutableStateOf(false) }
    val replies = remember { mutableStateListOf<Pair<String, String>>() }
    var isReplyFieldVisible by remember { mutableStateOf(false) }

    var showCreateSubtask by remember { mutableStateOf(false) }

    val taskRepository = remember { TaskRepository() }

    var showMemberSelection by remember { mutableStateOf(false) }
    var selectedMemberName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(taskId) {
        taskRepository.fetchTask(taskId, listId, projectId) { result, errorMsg ->
            task.value = result
            error.value = errorMsg
            result?.let { taskData ->
                descriptionText.value = taskData.description
                taskName.value = taskData.name
                comments.clear()

                taskStatus = result.status.toDatabaseString()
                taskPriority = result.priority?.toDatabaseString() ?: "Sem prioridade"

                taskData.comments.forEach { comment ->
                    val commentRequest = CommentRequest(
                        projectId = projectId,
                        listId = listId,
                        taskId = taskId,
                        comment = comment
                    )
                    comments.add(commentRequest)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    task.value?.let { it ->
                        if(isEditingName.value) {
                            OutlinedTextField(
                                value = taskName.value,
                                onValueChange = { taskName.value = it },
                                label = { Text("Editar Nome da Tarefa") },
                                modifier = Modifier.fillMaxWidth().testTag("nameField"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    focusedBorderColor = Color.White,
                                    unfocusedBorderColor = Color.Gray,
                                    cursorColor = Color.White
                                )
                            )
                        } else {
                            Text(
                                text = it.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth()
                                    .clickable { isEditingName.value = true }
                            )
                        }
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

                            if (isEditingDescription.value) {
                                OutlinedTextField(
                                    value = descriptionText.value,
                                    onValueChange = { descriptionText.value = it },
                                    label = { Text("Editar Descrição da Tarefa") },
                                    modifier = Modifier.fillMaxWidth().testTag("descriptionField"),
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
                                        isEditingName.value = false

                                        task.value?.let { updatedTask ->
                                            task.value = updatedTask.copy(
                                                name = taskName.value,
                                                description = descriptionText.value
                                            )

                                            val updatedTaskRequest = TaskRequest(
                                                projectId = projectId,
                                                listId = listId,
                                                task = updatedTask
                                            )

                                            TaskRepository().updateTask(updatedTaskRequest)

                                            Toast.makeText(
                                                context,
                                                "Descrição atualizada com sucesso!",
                                                Toast.LENGTH_SHORT
                                            ).show()
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

                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(0.dp, 50.dp, 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Status: ",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                )

                                val status = TaskStatus.entries
                                var expandedStatus by remember { mutableStateOf(false) }

                                OutlinedButton(
                                    onClick = { expandedStatus = true }
                                ) {
                                    Text(
                                        text = when (taskStatus) {
                                            "TODO" -> "A fazer"
                                            "DOING" -> "Fazendo"
                                            "DONE" -> "Feita"
                                            else -> "Status desconhecido"
                                        }
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null
                                    )
                                }

                                DropdownMenu(
                                    expanded = expandedStatus,
                                    onDismissRequest = { expandedStatus = false }
                                ) {
                                    status.forEach { status ->
                                        DropdownMenuItem(text = {
                                            Text(
                                                text = when (status.name) {
                                                    "TODO" -> "A fazer"
                                                    "DOING" -> "Fazendo"
                                                    "DONE" -> "Feita"
                                                    else -> "Status desconhecido"
                                                }
                                            )
                                        },
                                            onClick = {
                                                taskRepository.updateTaskStatus(
                                                    projectId,
                                                    listId,
                                                    taskId,
                                                    status
                                                )
                                                taskStatus = status.name
                                                expandedStatus = false
                                            })
                                    }
                                }

                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(0.dp, 50.dp, 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Prioridade: ",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 20.sp,
                                    color = Color.White
                                )

                                val priorities = Priority.entries
                                var expandedPriority by remember { mutableStateOf(false) }

                                OutlinedButton(
                                    onClick = { expandedPriority = true }
                                ) {
                                    Text(
                                        text = when (taskPriority) {
                                            "HIGH" -> "Alta"
                                            "MEDIUM" -> "Média"
                                            "LOW" -> "Baixa"
                                            else -> "Sem prioridade"
                                        }
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null
                                    )
                                }

                                DropdownMenu(
                                    expanded = expandedPriority,
                                    onDismissRequest = { expandedPriority = false }
                                ) {
                                    priorities.forEach { priority ->
                                        DropdownMenuItem(text = {
                                            Text(
                                                text = when (priority.name) {
                                                    "HIGH" -> "Alta"
                                                    "MEDIUM" -> "Média"
                                                    "LOW" -> "Baixa"
                                                    else -> "Sem prioridade"
                                                }
                                            )
                                        },
                                            onClick = {
                                                taskRepository.updateTaskPriority(
                                                    projectId,
                                                    listId,
                                                    taskId,
                                                    priority
                                                )
                                                taskPriority = priority.name
                                                expandedPriority = false
                                            })
                                    }
                                }


                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Text(
                                    text = "Atributos:",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 50.dp, 10.dp)
                                )

                                task.value?.attributes?.let { attributes ->
                                    attributes.forEach { attribute ->
                                        var attributeName by remember {
                                            mutableStateOf(
                                                attribute.attributeName ?: "Atributo sem nome"
                                            )
                                        }
                                        var attributeDescription by remember {
                                            mutableStateOf(
                                                attribute.attributeDescription
                                                    ?: "Atributo sem descrição"
                                            )
                                        }

                                        Column(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .background(Color.Gray)
                                                .fillMaxWidth()
                                        ) {
                                            OutlinedTextField(
                                                value = attributeName,
                                                onValueChange = { newName ->
                                                    attributeName = newName
                                                },
                                                label = { Text("Nome do Atributo") },
                                                modifier = Modifier.fillMaxWidth()
                                            )

                                            OutlinedTextField(
                                                value = attributeDescription,
                                                onValueChange = { newDescription ->
                                                    attributeDescription = newDescription
                                                },
                                                label = { Text("Descrição do Atributo") },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = 8.dp)
                                            )

                                            Button(
                                                onClick = {
                                                    task.value = task.value?.copy(
                                                        attributes = attributes.map {
                                                            if (it == attribute) {
                                                                it.copy(
                                                                    attributeName = attributeName,
                                                                    attributeDescription = attributeDescription
                                                                )
                                                            } else it
                                                        }
                                                    )
                                                    /*taskRepository.updateTaskAttribute(
                                                        projectId,
                                                        listId,
                                                        taskId,
                                                        attribute.copy(
                                                            attributeName = attributeName,
                                                            attributeDescription = attributeDescription
                                                        )
                                                    )*/
                                                },
                                                modifier = Modifier.padding(top = 8.dp)
                                            ) {
                                                Text("Salvar Alterações")
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 16.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(
                                            onClick = { /*navController.navigate("task_attribute_screen")*/ }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Adicionar Atributo",
                                                tint = Color(0xFF246BFD)
                                            )
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
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { showMemberSelection = true },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Selecionar Responsável")
                            IconButton(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(20.dp), //tamanho
                                onClick = { showMemberSelection = true }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.usermember),
                                    contentDescription = "Selecionar Responsável",
                                    tint = Color(0xFF246BFD)
                                )
                            }
                        }
                    }

                    selectedMemberName?.let {
                        Text(
                            text = "Responsável: $it",
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }
                    if (showMemberSelection) {
                        MemberSelectionModal(
                            projectId = projectId,
                            listId = listId,
                            taskId = taskId,
                            onMemberSelected = { member ->
                                selectedMemberName = member
                            },
                            onDismiss = {
                                showMemberSelection = false
                            }
                        )
                    }
                }
            }
            item{

                LazyColumn (modifier = Modifier
                    .heightIn(100.dp, 200.dp)
                    .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                    ){
                    task.value?.let {
                        items(it.subtasks){ subtask->
                            SubtaskItem(
                                subtask = subtask,
                                onDelete = { subtask._id?.let { it1 -> SubtaskRepository().deleteSubtask(projectId = projectId, listId = listId, taskId = taskId, subtaskId = it1) } }
                            )
                            }
                        }
                    }

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    Button(modifier = Modifier
                        .padding(16.dp)
                        .width(180.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF246BFD),
                            contentColor = Color.White
                        ), onClick = {showCreateSubtask = true}){
                        Text(text = "Adicionar subtarefa")
                    }
                }

                if(showCreateSubtask){
                    CreateSubtaskModalBottomSheet(onDismiss = {
                        showCreateSubtask = false
                    }, projectId = projectId, listId = listId, taskId = taskId)
                }

            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button( modifier = Modifier
                        .padding(16.dp)
                        .width(180.dp),
                        onClick = {
                            task.value?.let { currentTask ->

                                val duplicatedTask = currentTask.copy(
                                    _id = null,
                                    data = SimpleDateFormat(
                                        "dd/MM/yyyy",
                                        Locale.getDefault()
                                    ).format(Date())
                                )

                                val duplicatedTaskRequest = TaskRequest(
                                    projectId = projectId,
                                    listId = listId,
                                    task = duplicatedTask.copy(_id = null)
                                )

                                TaskRepository().postTasks(duplicatedTaskRequest)

                                Toast.makeText(
                                    context,
                                    "Tarefa duplicada com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF246BFD)),
                    ) {
                        Text(text = "Duplicar Tarefa", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Comentários:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )

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
                                val newComment = Comment(
                                    _id = null,
                                    data = currentDate,
                                    email = email,
                                    userId = null,
                                    text = commentText.value,
                                    replies = listOf()
                                )
                                val newCommentRequest = CommentRequest(projectId = projectId,listId = listId,taskId = taskId, comment = newComment)
                                taskRepository.postComment(newCommentRequest) { success, errorMsg ->
                                    if (success) {
                                        comments.add(newCommentRequest)
                                        commentText.value = ""
                                        Toast.makeText(context, "Comentário adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, errorMsg ?: "Erro ao adicionar comentário", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Por favor, preencha o comentário.", Toast.LENGTH_SHORT).show()
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

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                IconButton(onClick = { showReplies = !showReplies }) {
                                    Icon(
                                        painter = painterResource(id = if (showReplies) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
                                        contentDescription = if (showReplies) "Ocultar Respostas" else "Ver Respostas",
                                        tint = Color.White
                                    )
                                }

                                Button(
                                    onClick = {
                                        isReplyFieldVisible =
                                            !isReplyFieldVisible
                                        if (isReplyFieldVisible) {
                                            replyText.value = ""
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
                                        if (replyText.value.isNotBlank() && email.isNotBlank()) {
                                            replies.add(Pair(email, replyText.value))
                                            replyText.value = ""
                                            isReplyFieldVisible = false
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Por favor, preencha a resposta.",
                                                Toast.LENGTH_SHORT
                                            ).show()
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
                                            horizontalArrangement = Arrangement.End
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

@Composable
fun SubtaskItem(subtask: Subtask, onDelete: ()->Unit) {

    var clicked by remember {
        mutableStateOf(false)
    }

    var status by remember{
        mutableStateOf(subtask.status)
    }

    Box(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(0.9f)
            .padding(8.dp)
            .clickable {
                clicked = !clicked
            }
            .background(Color(0xFF1F222A), shape = RoundedCornerShape(15.dp))
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(30.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = subtask.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = subtask.dueDate,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
            Checkbox(
                checked = status==SubtaskStatus.DONE,
                onCheckedChange = { isChecked ->
                    val updatedStatus = if (isChecked) SubtaskStatus.DONE else SubtaskStatus.TODO
                    status = updatedStatus
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF246BFD),
                    uncheckedColor = Color(0xFF246BFD),
                    checkmarkColor = Color.White
                )
            )
        }
    }

    if(clicked){
        IconButton(modifier = Modifier.padding(16.dp), onClick = {onDelete()}){
            Icon(painter = painterResource(id = R.drawable.ic_trash), contentDescription = "Excluir subtarefa", tint = Color.Red)
        }
    }

}