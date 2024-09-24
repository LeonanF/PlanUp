package com.example.planup.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavHostController){

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    LaunchedEffect(Unit) {
        if (currentUser != null) {
            navController.navigate("project_list_screen"){
                popUpTo("home_screen"){ inclusive = true }
            }
        } else {

            navController.navigate("login_screen"){
                popUpTo("home_screen"){ inclusive = true }
            }
        }
    }

}