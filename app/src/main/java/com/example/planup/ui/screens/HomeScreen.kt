package com.example.planup.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController){

    navController.navigate("create_task"){
        popUpTo("home_screen") {inclusive = true}
    }

}