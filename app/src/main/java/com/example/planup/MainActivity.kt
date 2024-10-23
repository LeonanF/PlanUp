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
import com.example.planup.ui.components.DeleteModalBottomSheet
import com.example.planup.ui.screens.CreateTaskScreen
import com.example.planup.ui.screens.DeleteListScreen
import com.example.planup.ui.screens.HomeScreen
import com.example.planup.ui.screens.LoginScreen
import com.example.planup.ui.screens.MoveTaskScreen
//import com.example.planup.ui.screens.MoveTaskScreen
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
            composable("delete_modal_bottom_sheet/{taskId}/{projectId}/{listId}") { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId")
                val projectId = backStackEntry.arguments?.getString("projectId")
                val listId = backStackEntry.arguments?.getString("listId")
                DeleteModalBottomSheet(taskId = taskId!!, projectId = projectId!!, listId = listId!!) {
                    navController.popBackStack()
                }
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

            composable("delete_list_screen/{listId}/{projectId}") {
                backStackEntry ->
                val listId = backStackEntry.arguments?.getString("listId")
                val projectId = backStackEntry.arguments?.getString("projectId")
                DeleteListScreen(listId = listId!!, projectId = projectId!!) {
                    navController.popBackStack()
                }
            }
            composable("move_task_screen/{projectId}/{taskId}/{listId}") { backStackEntry ->
                val projectId = backStackEntry.arguments?.getString("projectId")
                val taskId = backStackEntry.arguments?.getString("taskId")
                val listId = backStackEntry.arguments?.getString("listId")

                MoveTaskScreen(projectId = projectId!!, taskId = taskId!!, listId = listId!!)
            }
        }
    }

}
