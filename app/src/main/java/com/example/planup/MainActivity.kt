package com.example.planup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planup.model.Project
import com.example.planup.ui.screens.CreateProjectScreen
import com.example.planup.ui.screens.CreateTaskScreen
import com.example.planup.ui.screens.HomeScreen
import com.example.planup.ui.screens.LoginScreen
import com.example.planup.ui.screens.ProjectListScreen
import com.example.planup.ui.screens.ProjectScreen
import com.example.planup.ui.screens.RegisterScreen
import com.example.planup.ui.screens.TaskAttributeScreen
import com.example.planup.ui.task.model.TaskViewModel
import com.example.planup.ui.theme.PlanUpTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var innerPadding : PaddingValues
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            PlanUpTheme {
                InitPadding()
                PlanUpNavHost()
            }
        }
    }

    @Composable
    fun InitPadding() {
        Scaffold { innerPadding ->
            this.innerPadding = innerPadding
        }
    }

    @Composable
    fun PlanUpNavHost() {
        navController = rememberNavController()

        LaunchedEffect(Unit) {
            if (currentUser != null) {
                navController.navigate("home_screen")
            } else {
                navController.navigate("login_screen")
            }
        }

        NavHost(navController, startDestination = "login_screen"
        ){
            composable("login_screen"){ LoginScreen(navController = navController)}
            composable("register_screen"){ RegisterScreen(navController = navController) }
            composable("create_project_screen"){ CreateProjectScreen(navController = navController ) }
            composable("create_task"){ CreateTaskScreen(navController = navController, viewModel = TaskViewModel()) }
            composable("project_list_screen"){ ProjectListScreen(navController = navController) }
            composable("home_screen"){ HomeScreen(navController = navController)}
            composable("task_attribute_screen/{taskId}") { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId")
                TaskAttributeScreen(navController, taskId)
            }
            composable("project_screen/{project}"){backStackEntry ->
                val projectJson = backStackEntry.arguments?.getString("project")
                val project = Gson().fromJson(projectJson, Project::class.java)
                ProjectScreen(navController = navController, project = project)
            }
        }
    }
}
