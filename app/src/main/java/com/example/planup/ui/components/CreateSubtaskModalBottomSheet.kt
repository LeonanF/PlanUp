package com.example.planup.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planup.R
import com.example.planup.model.ApiSubtask
import com.example.planup.model.Status
import com.example.planup.model.SubtaskRequest
import com.example.planup.repository.SubtaskRepository
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSubtaskModalBottomSheet(
    projectId : String,
    listId : String,
    taskId : String,
    onDismiss: () -> Unit
) {
    val dueDate = remember { mutableStateOf("") }
    val showDatePicker = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }

    var activeButton by remember {
        mutableStateOf(false)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(text = "Nova subtarefa",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(0.dp,0.dp,0.dp,20.dp),
                )

                HorizontalDivider(color = Color(0xFF35383F), thickness = 2.dp)

                OutlinedTextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it
                        activeButton = name.value.isNotBlank() && dueDate.value.isNotBlank()
                                    },
                    label = { Text("Nome da subtarefa", color = Color.White) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 10.dp)
                        .clickable{},
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFF35383F),
                        focusedContainerColor = Color(0xFF1F222A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                )


                Row(modifier = Modifier
                    .fillMaxWidth(0.9f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    OutlinedTextField(
                        value = dueDate.value,
                        onValueChange = {},
                        label = { Text("Data de entrega", color = Color.White) },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(vertical = 10.dp)
                            .clickable{},
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFF35383F),
                            focusedContainerColor = Color(0xFF1F222A),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        readOnly = true
                    )
                    IconButton(onClick = {showDatePicker.value=true}) {
                        Icon(painter = painterResource(id = R.drawable.ic_calendar), contentDescription = "Selecionar data", modifier = Modifier.size(48.dp))
                    }
                }

                Button(onClick = {
                    if(name.value.isNotBlank() && dueDate.value.isNotBlank()){
                        val subtask = SubtaskRequest(projectId = projectId, listId = listId, taskId = taskId,
                            ApiSubtask(name = name.value, status = Status.TODO.toDatabaseString(), dueDate = dueDate.value))
                        SubtaskRepository().postSubTask(subtask)
                        onDismiss.invoke()
                    }},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (activeButton) Color(0xFF246BFD) else Color(0xFF476EBE),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(0.9f).heightIn(90.dp).padding(0.dp, 15.dp)
                ){
                    Text("Criar subtarefa", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                if (showDatePicker.value) {
                    DatePickerModalInput(
                        onDateSelected = { selectedDateMillis ->
                            selectedDateMillis?.let {
                                dueDate.value = formatMillisToDate(it)
                                activeButton = dueDate.value.isNotBlank() && name.value.isNotBlank()
                            }
                            showDatePicker.value = false
                        },
                        onDismiss = { showDatePicker.value = false }
                    )
                }



            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun formatMillisToDate(timeInMillis: Long): String {
    val instant = Instant.ofEpochMilli(timeInMillis)

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.of("UTC"))

    return formatter.format(instant)
}