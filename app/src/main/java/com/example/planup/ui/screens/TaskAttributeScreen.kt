package com.example.planup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planup.model.Attribute
import com.example.planup.model.AttributeRequest
import com.example.planup.repository.AttributeRepository

@Composable
fun TaskAttributeScreen(navController: NavHostController? = null, taskId:String? = null){

    var attributeName by remember { mutableStateOf("") }
    var attributeDescription by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {  innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0XFF181A20))
                .padding(innerPadding)
        ) {
            Button(
                onClick = {},
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White

                ),
                modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }

            Box(
                modifier = Modifier
                    .padding(0.dp, 50.dp, 0.dp, 0.dp)
                    .fillMaxWidth(),
                Alignment.Center
            ){
                Text(
                    text = "Adicionar Atributo",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            TextField(
                label = {
                    Text(
                        text = "Nome",
                        color = Color.LightGray,
                        fontSize = 12.sp)},
                value = attributeName,
                onValueChange = {
                    attributeName = it
                },
                modifier = Modifier
                    .padding(20.dp, 50.dp, 20.dp, 0.dp)
                    .fillMaxWidth()
                    .height(100.dp),
                shape = Shapes().large,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray,
                    focusedContainerColor = Color(0xFF1F222A),
                    unfocusedContainerColor = Color(0xFF1F222A),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            TextField(
                label = {
                    Text(
                        text = "Descrição",
                        color = Color.LightGray,
                        fontSize = 12.sp)},
                value = attributeDescription,
                onValueChange = {
                    attributeDescription = it
                },
                modifier = Modifier
                    .padding(20.dp, 20.dp, 20.dp, 0.dp)
                    .fillMaxWidth(),
                shape = Shapes().large,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray,
                    focusedContainerColor = Color(0xFF1F222A),
                    unfocusedContainerColor = Color(0xFF1F222A),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )


            Box(
                modifier = Modifier
                    .padding(0.dp, 50.dp, 0.dp, 0.dp)
                    .fillMaxWidth(),
                Alignment.Center
            ){
                Button(
                    onClick = {
                        AttributeRepository().postAttribute(
                            attributeReq = AttributeRequest(Attribute(attributeName, attributeDescription),taskId!!)
                        )
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0XFF476EBE),
                        contentColor = Color.White

                    ),
                    modifier = Modifier
                        .padding(50.dp, 10.dp, 50.dp, 0.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Adicionar",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CreateTaskPreview(){
    TaskAttributeScreen()
}