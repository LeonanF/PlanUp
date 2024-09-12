package com.example.planup.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, innerPadding: PaddingValues){

    var showBottomOptions by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.BottomCenter
    ){
        Button(onClick = {showBottomOptions=true}) {
            Text("Open Create Project")
        }
    }

    if(showBottomOptions){
        ModalBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color(0xFF181A20),
            onDismissRequest = {showBottomOptions = false}) {
            CreateProjectScreen(navController, innerPadding)
        }
    }

}