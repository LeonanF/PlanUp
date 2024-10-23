package com.example.planup.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planup.R
import com.example.planup.repository.ProjectRepository
import com.example.planup.ui.components.AddMemberModalBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberScreen(
    navController: NavHostController,
    projectId : String
){

    var members by remember { mutableStateOf<List<String>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    var showAddMember by remember { mutableStateOf(false)}

    LaunchedEffect(projectId) {
        ProjectRepository().fetchMembers(projectId){ result, errorMsg ->
            members = result
            error = errorMsg
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                actions = {
                },
                title = {
                    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement =  Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.size(24.dp))
                        
                        Text(
                            "Membros do time (${if (!members.isNullOrEmpty()) members?.size else 0})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        )
                    }},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF181A20),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
            )
        },
        bottomBar = {
                BottomAppBar(actions = {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ){
                        
                        HorizontalDivider()

                        Spacer(modifier = Modifier.size(16.dp))
                        
                        Button(onClick = { showAddMember = true }, modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(65.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF246BFD),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(painter = painterResource(id = R.drawable.ic_add_member), contentDescription = "Botão de adicionar membro", tint = Color.White, modifier = Modifier.size(30.dp))
                            Spacer(modifier = Modifier.size(24.dp))
                            Text("Adicionar Membro", fontSize = 16.sp)
                        }
                    }
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    containerColor = Color.Transparent
                )

        },
        containerColor = Color(0xFF181A20)
    )
    {
            innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
        ) {
            error?.let {
                Text("Erro: $it", color = Color.Red)
            }

            members?.let { members ->
                LazyColumn (modifier = Modifier
                    .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    items(members){ member ->
                        Row(modifier = Modifier.fillMaxWidth().padding(10.dp, 0.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                            Icon(painter = painterResource(id = R.drawable.person), contentDescription = "Membro", tint = Color.Gray, modifier = Modifier.size(60.dp))
                            Text(member, color = Color.White, fontSize = 14.sp)
                            IconButton(onClick = {
                                ProjectRepository().deleteMember(memberId = member, projectId = projectId)
                                navController.navigate("member_screen/$projectId"){
                                    navController.popBackStack()
                                }
                            }) {
                                Icon(painter = painterResource(id = R.drawable.ic_trash), contentDescription = "Deletar membro", tint = Color.Red)
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

    if(showAddMember){
        AddMemberModalBottomSheet(projectId, onDismiss = {showAddMember=false})
    }

}