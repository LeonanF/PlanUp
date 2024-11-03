package com.example.planup.ui.components

import android.util.Log
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planup.model.ProjectDetailPreview
import com.example.planup.model.TaskListPreview
import com.example.planup.model.TaskListUpdateRequest
import com.example.planup.repository.ProjectRepository
import com.example.planup.repository.TaskListRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskListModalBottomSheet(
    projectId: String,
    listId: String,
    onDismiss: () -> Unit
) {
    val project = remember { mutableStateOf<ProjectDetailPreview?>(null) }
    val context = LocalContext.current
    val listName = remember {
        mutableStateOf("")
    }
    val taskListSelected = remember {
        mutableStateOf<TaskListPreview?>(null)
    }
    var activeButton by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = projectId) {
        ProjectRepository().fetchProjectPreview(projectId) { result, _ ->
            project.value = result
            result?.let { projectData ->
                projectData.taskLists.forEach { taskList ->
                    if (taskList._id == listId) {
                        listName.value = taskList.name
                        taskListSelected.value = taskList
                    }
                }
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier
            .heightIn(350.dp),
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF181A20)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Alterar nome do quadro",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(0.dp,0.dp,0.dp,20.dp),
            )

            HorizontalDivider(color = Color(0xFF35383F), thickness = 1.dp, modifier = Modifier.padding(20.dp, 0.dp))

            OutlinedTextField(
                value = listName.value,
                onValueChange = { newText ->
                    listName.value = newText
                    activeButton = newText.isNotBlank()
                },
                label = { Text("Editar nome do quadro", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(0.dp, 10.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF35383F),
                    focusedContainerColor = Color(0xFF1F222A),
                    focusedTextColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    val taskListUpdateRequest = TaskListUpdateRequest(
                        projectId = projectId,
                        listId = listId,
                        name = listName.value
                    )

                    TaskListRepository().updateTaskList(taskListUpdateRequest) { result ->
                        Log.d("TaskListRepository", "Mudando o nome da lista para ${listName.value}")
                        if (result) {
                            Toast.makeText(context, "Lista atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        } else {
                            Toast.makeText(context, "Não foi possível atualizar a lista.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeButton) Color(0xFF246BFD) else Color(0xFF476EBE),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .heightIn(65.dp)
            ){
                Text(
                    text = "Editar quadro",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}