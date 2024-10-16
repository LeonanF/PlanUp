package com.example.planup.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planup.R
import com.example.planup.model.ProjectPreview
import com.example.planup.repository.ProjectRepository
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectPreviewListScreen(
    onProjectClick: (String) -> Unit,
) {
    val projects = remember { mutableStateOf<List<ProjectPreview>?>(null) }
    val error = remember { mutableStateOf<String?>(null) }

    val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

    LaunchedEffect(userId) {
        ProjectRepository().fetchProjectPreviews(userId) { result, errorMsg ->
            projects.value = result
            error.value = errorMsg
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus projetos", fontWeight = FontWeight.Bold, fontSize = 26.sp) },
                actions = {
                    IconButton(onClick = { }, modifier = Modifier.padding(10.dp)) {
                        Icon(
                            painterResource(R.drawable.search_icon),
                            contentDescription = "Buscar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF181A20),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                modifier = Modifier.padding(30.dp,0.dp)
            )
        }
    ) { innerPadding ->

        projects.value?.let { projectPreviewList ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(projectPreviewList) { project ->
                    ProjectItem(
                        project = project,
                        onClick = {
                            onProjectClick(project._id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectItem(project: ProjectPreview, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = project.name, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = project.description)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Status: ${project.status}")
        }
    }
}