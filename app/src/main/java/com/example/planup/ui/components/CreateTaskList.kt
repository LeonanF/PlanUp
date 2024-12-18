package com.example.planup.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.planup.model.TaskList
import com.example.planup.model.TaskListRequest
import com.example.planup.repository.TaskListRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskList (projectId: String, onDismiss: () -> Unit) {
    var listName by remember {
        mutableStateOf("")
    }

    var activeButton by remember {
        mutableStateOf(false)
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
            Text(text = "Adicionar Novo Quadro",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(0.dp,0.dp,0.dp,20.dp),
            )

            HorizontalDivider(color = Color(0xFF35383F), thickness = 1.dp, modifier = Modifier.padding(20.dp, 0.dp))

            OutlinedTextField(value = listName, onValueChange = { newText ->
                listName = newText
                activeButton = newText.isNotBlank()
                },
                label = { Text("Nome do quadro", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(0.dp, 10.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF35383F),
                    focusedContainerColor = Color(0xFF1F222A),
                    focusedTextColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(onClick = {
                Log.d("CreateTaskList", "listName: $listName" + " Tipo: ${listName::class.simpleName}")
                TaskListRepository()
                    .postProjectList(
                        TaskListRequest(
                            projectId = projectId,
                            taskList = TaskList(
                                _id = null,
                                name = listName,
                                tasks = emptyList()
                            )
                        )
                    )
                onDismiss.invoke()
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeButton) Color(0xFF246BFD) else Color(0xFF476EBE),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(0.9f).heightIn(65.dp)
            ){
                Text(
                    text = "Criar Novo Quadro",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}