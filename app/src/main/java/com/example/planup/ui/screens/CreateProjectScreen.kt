package com.example.planup.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planup.model.Project
import com.example.planup.repository.ProjectRepository
import com.google.firebase.auth.FirebaseAuth


@Composable
fun CreateProjectScreen(navController: NavHostController){

    val projectName = remember {
        mutableStateOf("")
    }

    val projectDescription = remember {
        mutableStateOf("")
    }

        Box(modifier = Modifier
            .padding(bottom = 100.dp)
            .imePadding(),
            contentAlignment = Alignment.BottomCenter
        )
        {
            Column (modifier = Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp, 0.dp, 0.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    text = "Novo Projeto",
                    fontSize = 24.sp
                )
                HorizontalDivider(
                    modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 10.dp),
                    color = Color(0XFF35383F),
                )

                OutlinedTextField(value = projectName.value, onValueChange = { newText ->
                    projectName.value = newText
                },
                    label = {Text("Nome do projeto", color = Color.White)},
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(0.dp, 10.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFF1F222A),
                        focusedContainerColor = Color(0xFF1F222A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                OutlinedTextField(value = projectDescription.value, onValueChange = { newText ->
                    projectDescription.value = newText
                },
                    label = {Text("Descrição do projeto", color = Color.White)},
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(0.dp, 10.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFF1F222A),
                        focusedContainerColor = Color(0xFF1F222A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(25.dp))

                Button(onClick = {
                    if(projectName.value.isNotBlank()){
                        navController.navigate("home_screen"){
                            val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                            ProjectRepository()
                                .postProject(
                                    Project(
                                        name= projectName.value,
                                        description = projectDescription.value,
                                        owner = userid,
                                        taskLists = null,
                                        members = listOf(userid),
                                        status = null
                                    )
                                )
                            popUpTo("create_project_screen"){inclusive = true}
                        }
                    }
                },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF476EBE),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Criar projeto", fontSize = 16.sp)
                }
            }


    }
}

/*
@Preview
@Composable
fun DynamicBoxExample() {
    var isBoxVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Button(onClick = { isBoxVisible = !isBoxVisible }) {
            Text(text = if (isBoxVisible) "Hide Box" else "Show Box")
        }

        Spacer(modifier = Modifier.height(16.dp))


        if (isBoxVisible) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Red)
            ) {
                Text(
                    text = "I'm a Box!",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
 */