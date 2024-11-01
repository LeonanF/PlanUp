package com.example.planup.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planup.R
import com.example.planup.model.User
import com.example.planup.repository.UserRepository
import com.example.planup.ui.components.SignOutModalBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController? = null, userId: String? = null, qtdProjects: Int? = null, qtdTasks: Int? = null) {
    val user = remember { mutableStateOf<User?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val showSignOutModalBottomSheet = remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Carrega a imagem salva ao iniciar a tela
    LaunchedEffect(key1 = Unit, key2 = userId) {
        imageUri = UserRepository().loadImageFromInternalStorage(context)

        if (userId != null) {
            UserRepository().fetchUser(userId) { result ->
                user.value = result
            }
        }
    }

    // Inicializa o seletor de imagens da galeria
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Salva a imagem no armazenamento local e atualiza o URI da imagem
            val result = UserRepository().saveImageToInternalStorage(context, uri)
            if (result.success) {
                imageUri = result.uri
                errorMessage = null
            } else {
                errorMessage = result.errorMessage
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF181A20),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Surface(
                            color = Color(0XFF246BFD),
                            shape = RoundedCornerShape(5.dp),
                            modifier = Modifier.size(25.dp),
                            shadowElevation = 4.dp
                        ) {
                            Icon(imageVector = Icons.Filled.Check, contentDescription = "Check")
                        }

                        Text(
                            text = "Perfil",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            modifier = Modifier.padding(start = 16.dp)
                        )

                        Box(
                            modifier = Modifier.fillMaxWidth(0.8F)
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.inbox),
                            contentDescription = "Inbox"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF181A20),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF181A20)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            UserImageField(imageUri) { galleryLauncher.launch("image/*") }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Clique na imagem para alterar",
                color = Color.White,
                fontSize = 12.sp
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = user.value?.nome ?: "Nome completo",
                color = Color.White,
                fontSize = 24.sp,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W700)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = user.value?.nomeDeUsuario ?: "Nome de usuário",
                color = Color.White,
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = qtdProjects?.toString() ?: "0",
                    color = Color.White,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W700)
                )

                Text(
                    text = qtdTasks?.toString() ?: "0",
                    color = Color.White,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W700)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "Projetos",
                    color = Color.White,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal)
                )

                Text(
                    text = "Tarefas",
                    color = Color.White,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(color = Color(0xFF35383F), thickness = 1.dp, modifier = Modifier.padding(20.dp, 0.dp))

            Spacer(modifier = Modifier.height(40.dp))

            Row(
               modifier = Modifier
                   .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            /* TODO NOT YET IMPLEMENTED */
                        }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Editar perfil",
                        tint = Color.White
                    )

                    Text(
                        text = "Editar perfil",
                        color = Color.White,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal),
                        modifier = Modifier
                            .padding(start = 30.dp)
                            .align(Alignment.CenterEnd)
                    )
                }

                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            showSignOutModalBottomSheet.value = true
                        }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ExitToApp,
                        contentDescription = "Sair",
                        tint = Color(0XFFF75555),
                    )

                    Text(
                        text = "Sair",
                        color = Color(0XFFF75555),
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal),
                        modifier = Modifier
                            .padding(start = 30.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                Alignment.Center
            ) {
                Button(
                    onClick = {
                        navController?.navigate("delete_account_screen") {
                            popUpTo("profile_screen/${userId}/${qtdProjects}/${qtdTasks}") {inclusive = true}
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0XFFF75555),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .heightIn(55.dp)
                        .fillMaxWidth()
                        .padding(20.dp, 0.dp)
                ) {
                    Text(
                        text = "Excluir conta",
                        color = Color.White,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                Alignment.Center
            ) {
                Button(
                    onClick = {
                        navController?.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF35383F),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .heightIn(55.dp)
                        .fillMaxWidth()
                        .padding(20.dp, 0.dp)
                ) {
                    Text(
                        text = "Voltar",
                        color = Color.White,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )
                }
            }
        }
    }

    if(showSignOutModalBottomSheet.value) {
        SignOutModalBottomSheet(navController) {
            showSignOutModalBottomSheet.value = false
            navController?.navigate("profile_screen/${userId}/${qtdProjects}/${qtdTasks}") {
                popUpTo("profile_screen/${userId}/${qtdProjects}/${qtdTasks}") {inclusive = true}
            }
        }
    }
}

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen()
}