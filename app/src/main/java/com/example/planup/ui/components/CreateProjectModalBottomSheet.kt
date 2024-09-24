package com.example.planup.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.example.planup.model.Project
import com.example.planup.repository.ProjectRepository
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectModalBottomSheet(
    onDismiss: () -> Unit
){

    val projectName = remember {
        mutableStateOf("")
    }

    val projectDescription = remember {
        mutableStateOf("")
    }

    var activeButton by remember {
        mutableStateOf(false)
    }

    ModalBottomSheet(
        modifier = Modifier
            .heightIn(450.dp),
        onDismissRequest = onDismiss
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Novo projeto",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(0.dp,0.dp,0.dp,20.dp),
                )
            
            HorizontalDivider(color = Color(0xFF35383F), thickness = 2.dp)

            OutlinedTextField(value = projectName.value, onValueChange = { newText ->
                projectName.value = newText
                activeButton = newText.isNotBlank()
            },
                label = {Text("Nome do projeto", color = Color.White)},
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(0.dp, 10.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF35383F),
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
                    unfocusedContainerColor = Color(0xFF35383F),
                    focusedContainerColor = Color(0xFF1F222A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(onClick = {
                if(projectName.value.isNotBlank()){
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
                    onDismiss.invoke()
                }},
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeButton) Color(0xFF246BFD) else Color(0xFF476EBE),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(0.9f).heightIn(65.dp)
            ){
                Text("Criar projeto", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }


        }
    }
}