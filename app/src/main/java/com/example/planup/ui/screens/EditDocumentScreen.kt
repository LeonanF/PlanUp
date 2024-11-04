package com.example.planup.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.planup.model.UpdateDocumentRequest
import com.example.planup.repository.TaskRepository

@Composable
fun EditDocumentScreen(
    navController : NavHostController,
    projectId : String,
    listId : String,
    taskId : String,
    documentId : String
){
    var documentTitle by remember { mutableStateOf("") }
    var documentText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(documentId) {
        TaskRepository().fetchDocument(
            projectId = projectId,
            listId = listId,
            taskId = taskId,
            documentId = documentId
        ) { document, error ->
            if (document != null) {
                documentTitle = document.title
                documentText = document.text
            } else if (error != null) {
                errorMessage = error
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        IconButton(modifier = Modifier.padding(top = 32.dp), onClick = {
            navController.navigate("task_detail_screen/$projectId/$listId/$taskId"){
                popUpTo("edit_document_screen/$projectId/$listId/$taskId/$documentId"){
                    inclusive = true
                }
            }
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }

        OutlinedTextField(
            value = documentTitle,
            onValueChange = { newTitle ->
                if (newTitle.length in 0..100) {
                    documentTitle = newTitle
                }
            },
            label = { Text("Documento") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White
            )
        )

        TextField(
            value = documentText,
            onValueChange = { newText : String -> documentText = newText },
            placeholder = { Text("Digite o conteúdo do documento aqui...") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            maxLines = Int.MAX_VALUE,
            singleLine = false
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    navController.navigate("task_detail_screen/$projectId/$listId/$taskId"){
                        popUpTo("edit_document_screen/$projectId/$listId/$taskId/$documentId"){
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Descartar")
            }
            Button(onClick = {
                TaskRepository().updateDocument(UpdateDocumentRequest(
                    projectId = projectId,
                    listId = listId,
                    taskId = taskId,
                    documentId = documentId,
                    title = documentTitle,
                    text = documentText
                )
                )
                navController.navigate("task_detail_screen/$projectId/$listId/$taskId"){

                    popUpTo("edit_document_screen/$projectId/$listId/$taskId/$documentId"){
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }) {
                Text("Salvar")
            }
        }
    }
}