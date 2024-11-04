package com.example.planup.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planup.model.SubtaskStatus
import com.example.planup.model.Task
import com.example.planup.model.UpdateSubtaskRequest
import com.example.planup.repository.SubtaskRepository
import com.example.planup.repository.TaskRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateSubtaskModalBottomSheet(
    projectId: String,
    listId: String,
    taskId: String,
    subtaskId: String,
    onDismiss: () -> Unit
) {
    val name = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }
    val status = remember {
        mutableStateOf("")
    }
    val task = remember { mutableStateOf<Task?>(null) }
    val error = remember { mutableStateOf<String?>(null) }
    val showDatePicker = remember { mutableStateOf(false) }
    var activeButton by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = taskId) {
        TaskRepository().fetchTask(taskId, listId, projectId) { result, erro ->
            task.value = result
            error.value = erro
            result?.let { taskData ->
                taskData.subtasks.find { subtask ->
                    subtask._id == subtaskId
                }?.let { subtask ->
                    name.value = subtask.name
                    date.value = subtask.dueDate
                    status.value = subtask.status.name
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
            Text(text = "Editar Subtask",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(0.dp,0.dp,0.dp,20.dp),
            )

            HorizontalDivider(color = Color(0xFF35383F), thickness = 1.dp, modifier = Modifier.padding(20.dp, 0.dp))

            OutlinedTextField(
                value = name.value,
                onValueChange = { newText ->
                    name.value = newText
                    activeButton = newText.isNotBlank()
                },
                label = { Text("Editar nome da subtask", color = Color.LightGray) },
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

            OutlinedTextField(
                value = date.value,
                onValueChange = {},
                label = { Text("Editar data de entrega", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(0.dp, 10.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF35383F),
                    focusedContainerColor = Color(0xFF1F222A),
                    focusedTextColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                showDatePicker.value = true
                            }
                    )
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    val updatedSubtask = UpdateSubtaskRequest(
                        projectId = projectId,
                        listId =  listId,
                        taskId =  taskId,
                        subtaskId =  subtaskId,
                        status = status.value,
                        name = name.value,
                        dueDate =  date.value
                    )

                    SubtaskRepository().updateSubtask(updatedSubtask)
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
                    text = "Editar subtarefa",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

    if (showDatePicker.value) {
        DatePickerModalInput(
            onDateSelected = { selectedDateMillis ->
                selectedDateMillis?.let {
                    date.value = formatMillisToDate(it)
                }
                showDatePicker.value = false
            },
            onDismiss = { showDatePicker.value = false }
        )
    }
}