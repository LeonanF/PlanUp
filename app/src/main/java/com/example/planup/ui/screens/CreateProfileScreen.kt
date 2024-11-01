package com.example.planup.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.transform.CircleCropTransformation
import com.example.planup.R
import com.example.planup.auth.EmailAndPasswordAuth
import com.example.planup.model.User
import com.example.planup.repository.UserRepository
import com.example.planup.ui.components.DatePickerModalInput
import com.example.planup.ui.components.formatMillisToDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(navController: NavHostController? = null) {
    var nome by remember { mutableStateOf("") }
    var nomeUsuario by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    val showDatePicker = remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("") }
    val activeButton = remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
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
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                navController?.popBackStack()
                            }
                    )
                },
                title = {
                    Text(
                        text = "Preencha o seu Perfil",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF181A20),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                modifier = Modifier.padding(20.dp, 10.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF181A20))
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserImageField(imageUri) { galleryLauncher.launch("image/*") }

            Spacer(modifier = Modifier.height(10.dp))

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

            TextField(
                value = nome,
                onValueChange = {
                    nome = it
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
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = "Nome completo",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                },
                singleLine = true
            )

            TextField(
                value = nomeUsuario,
                onValueChange = {
                    nomeUsuario = it
                },
                modifier = Modifier
                    .padding(20.dp, 10.dp, 20.dp, 0.dp)
                    .fillMaxWidth(),
                shape = Shapes().large,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray,
                    focusedContainerColor = Color(0xFF1F222A),
                    unfocusedContainerColor = Color(0xFF1F222A),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = "Usuário",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                },
                singleLine = true
            )

            TextField(
                value = dataNascimento,
                onValueChange = {},
                modifier = Modifier
                    .padding(20.dp, 10.dp, 20.dp, 0.dp)
                    .fillMaxWidth(),
                shape = Shapes().large,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray,
                    focusedContainerColor = Color(0xFF1F222A),
                    unfocusedContainerColor = Color(0xFF1F222A),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                showDatePicker.value = true
                            }
                    )
                },
                label = {
                    Text(
                        text = "Data de nascimento",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                },
                singleLine = true
            )

            TextField(
                value = email,
                onValueChange = {
                    email = it
                },
                modifier = Modifier
                    .padding(20.dp, 10.dp, 20.dp, 0.dp)
                    .fillMaxWidth(),
                shape = Shapes().large,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray,
                    focusedContainerColor = Color(0xFF1F222A),
                    unfocusedContainerColor = Color(0xFF1F222A),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.MailOutline,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = "E-mail",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                },
                singleLine = true
            )

            TextField(
                value = genero,
                onValueChange = {
                    genero = it
                },
                modifier = Modifier
                    .padding(20.dp, 10.dp, 20.dp, 0.dp)
                    .fillMaxWidth(),
                shape = Shapes().large,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray,
                    focusedContainerColor = Color(0xFF1F222A),
                    unfocusedContainerColor = Color(0xFF1F222A),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Face,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = "Gênero",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                },
                singleLine = true
            )

            TextField(
                value = cargo,
                onValueChange = {
                    cargo = it
                },
                modifier = Modifier
                    .padding(20.dp, 10.dp, 20.dp, 0.dp)
                    .fillMaxWidth(),
                shape = Shapes().large,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray,
                    focusedContainerColor = Color(0xFF1F222A),
                    unfocusedContainerColor = Color(0xFF1F222A),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = "Cargo",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                Alignment.Center
            ) {
                Button(
                    onClick = {
                        activeButton.value = nome.isNotBlank() &&
                                nomeUsuario.isNotBlank() &&
                                dataNascimento.isNotBlank() &&
                                email.isNotBlank() &&
                                cargo.isNotBlank()

                        if (activeButton.value) {
                            val newUser = User(
                                id = EmailAndPasswordAuth().getCurrentUser()?.uid.toString(),
                                nome = nome,
                                nomeDeUsuario = nomeUsuario,
                                dataNascimento = dataNascimento,
                                email = email,
                                genero = genero,
                                cargo = cargo
                            )

                            UserRepository().postUser(newUser) { resultado ->
                                if (resultado) {
                                    Toast.makeText(context, "Perfil criado com sucesso!", Toast.LENGTH_SHORT).show()
                                    navController?.navigate("home_screen") {
                                        popUpTo("create_profile_screen") { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context, "Erro ao criar perfil!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(activeButton.value) Color(0XFF246BFD) else Color(0xFF476EBE),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.heightIn(50.dp)
                        .padding(bottom = 10.dp)
                ) {
                    Text(
                        text = "Continuar",
                        color = Color.White,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W700)
                    )
                }
            }

            if (showDatePicker.value) {
                DatePickerModalInput(
                    onDateSelected = { selectedDateMillis ->
                        selectedDateMillis?.let {
                            dataNascimento = formatMillisToDate(it)
                        }
                        showDatePicker.value = false
                    },
                    onDismiss = { showDatePicker.value = false }
                )
            }
        }
    }
}

@Composable
@Preview
fun CreateProfileScreenPreview() {
    CreateProfileScreen()
}

@Composable
fun UserImageField(imageUri: Uri?, onImageClick: () -> Unit) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(data = imageUri ?: "https://via.placeholder.com/150").apply(block = fun ImageRequest.Builder.() {
                transformations(CircleCropTransformation())
            }).build()
    )

    if(imageUri != null) {
        Image(
            painter = painter,
            contentDescription = "User Image",
            modifier = Modifier
                .size(100.dp)
                .clickable { onImageClick() }
                .padding(4.dp),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.default_user_image),
            contentDescription = "User Image",
            modifier = Modifier
                .size(100.dp)
                .clickable { onImageClick() }
                .padding(4.dp),
            contentScale = ContentScale.Crop
        )
    }
}