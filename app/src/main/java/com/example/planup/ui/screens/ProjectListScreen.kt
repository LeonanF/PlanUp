package com.example.planup.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planup.R
import com.example.planup.model.ProjectPreview
import com.example.planup.repository.ProjectRepository
import com.example.planup.ui.components.CreateProjectModalBottomSheet
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(navController: NavHostController) {

    val projects = remember { mutableStateOf<List<ProjectPreview>?>(null) }
    val error = remember { mutableStateOf<String?>(null) }

    var showCreateProject by remember {
        mutableStateOf(false)
    }

    val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

    LaunchedEffect(userId, showCreateProject) {
        ProjectRepository().fetchProjectPreviews(userId) { result, errorMsg ->
            projects.value = result
            error.value = errorMsg
        }
    }

    Scaffold (
            topBar = {
                TopAppBar(
                    title = { Text("Meus projetos", fontWeight = FontWeight.Bold, fontSize = 26.sp) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF181A20),
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                    modifier = Modifier.padding(30.dp,0.dp)
                )
            },
            bottomBar = {
              Box{
                BottomAppBar(actions = {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                        BottomBarItem(iconRes = R.drawable.projects, label = "Projetos", onClick = {
                            navController.navigate("project_list_screen"){
                                popUpTo("project_list_screen"){ inclusive = true }
                            }
                        })
                        BottomBarItem(iconRes = R.drawable.person, label = "Perfil", onClick = {
                            navController.navigate("profile_screen/${projects.value?.size}/${projects.value?.sumOf { it.taskQuantity }}") {
                                popUpTo("project_list_screen"){ inclusive = false }
                            }
                        })
                    }
                },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.Transparent
                )
            }},
        containerColor = Color(0xFF181A20),
        modifier = Modifier.fillMaxSize()
    ) {
        innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
        ) {
            error.value?.let {
                Text("Erro: $it", color = Color.Red)
            }

            projects.value?.let { projectList ->
                LazyColumn (modifier = Modifier
                    .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    items(projectList) { project ->
                        Box(modifier = Modifier
                            .padding(20.dp,10.dp)
                            ){
                            Button(onClick = {
                                navController.navigate("project_detail_screen/${project._id}"){
                                    popUpTo("project_list_screen"){ inclusive = false }
                                }
                                             }, modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(200.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1F222A),
                                    contentColor = Color.White
                                )
                            ){
                                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start){
                                    Text(project.name, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(project.description, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text("Quantidade de tarefas: ${project.taskQuantity}", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.height(24.dp))
                                }
                            }
                        }
                    }
                }
            } ?: run {
                Scaffold(
                    containerColor = Color(0xFF181A20)
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator(modifier = Modifier.padding(innerPadding))
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp, end = 20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                Surface(
                    color = Color(0XFF246BFD),
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier.size(50.dp),
                    shadowElevation = 4.dp
                ) {
                    IconButton(
                        onClick = {
                            showCreateProject = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Criar novo projeto",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }
        }
    }

    if(showCreateProject){
        CreateProjectModalBottomSheet(onDismiss = {
            showCreateProject = false
            navController.navigate("project_list_screen") {
                popUpTo("project_list_screen"){ inclusive = true }
            }
        })
    }

}

@Composable
fun BottomBarItem(iconRes: Int, label: String, size: Dp = 30.dp, onClick: () -> Unit){

        IconButton(onClick = onClick, modifier = Modifier
            .fillMaxHeight()
            .widthIn(70.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Image(
                    painterResource(id = iconRes),
                    contentDescription = label,
                    modifier = Modifier.size(size)
                )
                Text(text = label, color = Color(0xFF9E9E9E))
        }
    }
}