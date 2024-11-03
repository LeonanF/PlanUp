package com.example.planup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planup.ui.components.CreateTaskList
import com.example.planup.ui.screens.CreateProfileScreen
import com.example.planup.ui.screens.CreateTaskScreen
import com.example.planup.ui.screens.DeleteAccountScreen
import com.example.planup.ui.screens.HomeScreen
import com.example.planup.ui.screens.LoginScreen
import com.example.planup.ui.screens.MemberScreen
import com.example.planup.ui.screens.ProfileScreen
import com.example.planup.ui.screens.ProjectDetailScreen
import com.example.planup.ui.screens.ProjectListScreen
import com.example.planup.ui.screens.RegisterScreen
import com.example.planup.ui.screens.TaskAttributeScreen
import com.example.planup.ui.screens.TaskDetailScreen
import com.example.planup.ui.theme.PlanUpTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var innerPadding : PaddingValues

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

        NavHost(navController, startDestination = "home_screen"
        ){
            composable("login_screen"){ LoginScreen(navController = navController)}
            composable("register_screen"){ RegisterScreen(navController = navController) }
            composable("create_task_screen/{projectId}/{listId}"){
                backStackEntry ->
                val projectId = backStackEntry.arguments!!.getString("projectId")
                val listId = backStackEntry.arguments!!.getString("listId")
                CreateTaskScreen(navController = navController, projectId = projectId!!, listId = listId!!)
            }
            composable("task_detail_screen/{taskId}/{projectId}/{listId}") {
                backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId")
                val projectId = backStackEntry.arguments?.getString("projectId")
                val listId = backStackEntry.arguments?.getString("listId")
                TaskDetailScreen(navController = navController, taskId = taskId!!,projectId = projectId!!, listId = listId!!)
            }
            composable("project_list_screen"){ ProjectListScreen(navController = navController) }

            composable("home_screen"){ HomeScreen(navController = navController)}

            composable("task_attribute_screen/{taskId}") { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId")
                TaskAttributeScreen(navController, taskId)
            }
            composable("create_task_list/{projectId}") { backStackEntry ->
                backStackEntry.arguments?.getString("projectId")?.let {
                    CreateTaskList(it) {
                        navController.popBackStack()
                    }
                }
            }
            composable("project_detail_screen/{projectId}") { backStackEntry ->
                val projectId = backStackEntry.arguments?.getString("projectId")
                ProjectDetailScreen(navController = navController, projectId = projectId!!)
            }
            composable("member_screen/{projectId}") {
                    backStackEntry ->
                val projectId = backStackEntry.arguments?.getString("projectId")
                MemberScreen(projectId = projectId!!, navController = navController)
            }
            composable("delete_account_screen") {
                DeleteAccountScreen(navController = navController)
            }
            composable("create_profile_screen") {
                CreateProfileScreen(navController = navController)
            }
            composable("profile_screen/{userId}/{qtdProject}/{qtdTask}") { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")
                val qtdProject = backStackEntry.arguments?.getString("qtdProject")?.toInt()
                val qtdTask = backStackEntry.arguments?.getString("qtdTask")?.toInt()
                ProfileScreen(navController = navController, userId = userId!!, qtdProjects = qtdProject!!, qtdTasks = qtdTask!!)
            }
        }
    }
}
