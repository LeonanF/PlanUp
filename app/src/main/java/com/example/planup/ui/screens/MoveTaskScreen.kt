package com.example.planup.ui.screens
/*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.planup.model.Task
import com.example.planup.model.TaskList
import com.example.planup.repository.TaskListRepository
import com.example.planup.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MoveTaskScreen(
    projectId: String,
    taskId: String
) {
    var lists by remember { mutableStateOf<List<TaskList>?>(null) }
    val task = remember { mutableStateOf<Task?>(null) }
    var selectedList by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var listError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = projectId) {
        TaskListRepository().fetchProjectLists(projectId) { result, msg ->
            lists = result
            listError = msg
        }
    }

    LaunchedEffect(key1 = taskId) {
        TaskRepository().fetchTask(taskId) { result, msg ->
            task.value = result
            error = msg
        }
    }

    Column {
        Text("Mover tarefa: ${task.value?.name}")

        lists?.let {
            MoveTaskDropDown(lists = it) { listId ->
                selectedList = listId
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (selectedList.isNotEmpty()) {
                    isLoading = true

                    CoroutineScope(Dispatchers.IO).launch {
                        task.value?._id?.let { id ->
                            TaskRepository().moveTask(
                                projectId = projectId,
                                taskId = id,
                                destinationList = selectedList
                            )
                        }
                        withContext(Dispatchers.Main) {
                            isLoading = false
                        }
                    }
                }
            }
        ) {
            Text(text = "Mover Tarefa")
        }

        if (isLoading) {
            CircularProgressIndicator()
        }

        error?.let {
            Text(text = it, color = Color.Red)
        }
    }
}

@Composable
fun MoveTaskDropDown(lists: List<TaskList>, onListSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedList by remember { mutableStateOf<TaskList?>(null) }

    Box {
        selectedList?.let { listName ->
            Text(
                text = listName.name,
                modifier = Modifier.clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                lists.forEach { list ->
                    DropdownMenuItem(
                        text = {
                            Text(text = list.name)
                        },
                        onClick = {
                            selectedList = list
                            expanded = false
                            list._id?.let { listId ->
                                onListSelected(listId)
                            }
                        }
                    )
                }
            }
        }
    }
}*/