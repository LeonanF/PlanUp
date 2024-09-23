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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.planup.model.Project
import com.example.planup.repository.ProjectRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(navController: NavHostController) {

    val projects = remember { mutableStateOf<List<Project>?>(null) }
    val error = remember { mutableStateOf<String?>(null) }

    val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

    LaunchedEffect(userId) {
        ProjectRepository().fetchUserProjects(userId) { result, errorMsg ->
            projects.value = result
            error.value = errorMsg
        }
    }

    Scaffold (
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
            },
            bottomBar = {
              Box(){
                BottomAppBar(actions = {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                        BottomBarItem(iconRes = R.drawable.home, label = "Home", onClick = {
                            navController.navigate("home_screen"){
                                popUpTo("project_list_screen"){ inclusive = true }
                            }
                        })
                        BottomBarItem(iconRes = R.drawable.projects, label = "Projetos", onClick = {
                            navController.navigate("project_list_screen"){
                                popUpTo("project_list_screen"){ inclusive = true }
                            }
                        })
                        BottomBarItem(iconRes = R.drawable.createbtn, label = "", 45.dp, onClick = {
                            navController.navigate("create_project_screen"){
                                popUpTo("project_list_screen"){ inclusive = true }
                            }
                        })
                        BottomBarItem(iconRes = R.drawable.inbox, label = "Inbox", onClick = {})
                        BottomBarItem(iconRes = R.drawable.person, label = "Perfil", onClick = {})
                    }
                },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.Transparent
                )
            }},
        containerColor = Color(0xFF181A20))
    {
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
                                val projectJson = Gson().toJson(project)
                                navController.navigate("project_screen/$projectJson"){
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
                                    val taskQuantity = project.taskLists?.size ?: 0
                                    val description = project.description ?: ""
                                    Text(project.name, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(description, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(taskQuantity.toString(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
        }
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