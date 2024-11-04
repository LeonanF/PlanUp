package com.example.planup.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planup.model.ProjectDetailPreview
import com.example.planup.model.Task
import com.example.planup.model.TaskRequest
import com.example.planup.repository.ProjectRepository
import com.example.planup.repository.TaskRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoveTaskModalBottomSheet(
    projectId: String,
    taskId: String,
    originListId: String,
    onDismiss: () -> Unit
) {
    val project = remember { mutableStateOf<ProjectDetailPreview?>(null) }
    val task = remember { mutableStateOf<Task?>(null) }
    val error = remember { mutableStateOf<String?>(null) }
    var selectedList by remember { mutableStateOf("") }
    var taskName by remember { mutableStateOf("") }
    var selectedTaskId by remember { mutableStateOf("") }
    var listName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = projectId, key2 = taskId) {
        ProjectRepository().fetchProjectPreview(projectId) { result, _ ->
            project.value = result
        }

        TaskRepository().fetchTask(taskId, originListId, projectId) { result, erro ->
            task.value = result
            error.value = erro
            result?.let { taskData ->
                taskName = taskData.name
                selectedTaskId = taskData._id.toString()
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier
            .heightIn(450.dp),
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF181A20)
    ) {
        Log.d("MoveTask", "Movendo tarefa $taskId da lista $originListId do projeto $projectId")

        Column (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Mover tarefa ")
                    withStyle(style = SpanStyle(color = Color(0XFFF75555), fontWeight = FontWeight.Bold)) {
                        append(taskName)
                    }
                    append(" para a lista ")
                },
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(20.dp, 0.dp, 20.dp, 5.dp)
                    .width(380.dp),
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Text(
                text = if(selectedList.isNotBlank()) listName else "",
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(20.dp, 0.dp, 20.dp, 5.dp)
                    .width(380.dp),
                color = Color(0XFFF75555),
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            HorizontalDivider(
                color = Color(0xFF35383F),
                thickness = 1.dp,
                modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            project.value?.taskLists?.let { taskLists ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(taskLists) { list ->
                        if(list._id != originListId) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF35383F))
                                    .padding(0.dp, 5.dp),
                                Alignment.Center
                            ) {
                                Text(
                                    text = list.name,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                                    color = Color.White,
                                    modifier = Modifier.clickable {
                                        list._id?.let { listId ->
                                            selectedList = listId
                                        }
                                        listName = list.name
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(5.dp))

            Button(
                onClick = {
                    if (selectedList.isNotEmpty()) {
                        isLoading = true

                        val movedTask = Task(
                            _id = task.value?._id,
                            name = taskName,
                            description = task.value?.description!!,
                            data = task.value?.data!!,
                            priority = task.value?.priority,
                            status = task.value?.status!!,
                            attributes = task.value?.attributes!!,
                            comments = task.value?.comments!!,
                            subtasks = task.value?.subtasks!!
                        )

                        TaskRepository().postTasks(
                            TaskRequest(
                                projectId = projectId,
                                listId = selectedList,
                                task = movedTask
                            )
                        )

                        Log.d("MoveTask", "Movendo tarefa ${task.value?._id} da lista $originListId para a lista $selectedList")

                        TaskRepository().deleteTask(projectId, originListId, taskId) {
                            isLoading = false
                            onDismiss()
                        }
                    } else {
                        Toast.makeText(context, "Selecione uma lista", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF246BFD),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .heightIn(45.dp)
            ){
                Text("Mover tarefa", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(5.dp))

            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0XFFF75555),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .heightIn(45.dp)
            ){
                Text("Cancelar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}