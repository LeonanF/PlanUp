package com.example.planup.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planup.R
import com.example.planup.auth.EmailAndPasswordAuth
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController) {
  var email by remember {
    mutableStateOf("")
  }

  var senha by remember {
    mutableStateOf("")
  }

  var senhaVisivel by remember {
    mutableStateOf(false)
  }

  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(snackbarHostState) }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxHeight()
        .background(Color(0XFF181A20))
        .padding(innerPadding)
    ) {
      Button(
        modifier = Modifier
          .width(75.dp)
          .padding(0.dp, 10.dp, 0.dp, 0.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
          containerColor = Color.Transparent,
          contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp, 0.dp, 10.dp, 0.dp),
        onClick = { /*TODO*/ }
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier
            .size(25.dp)
        )
      }

      Text(
        text = "Entre na sua conta",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(20.dp,40.dp,0.dp,0.dp)
      )

      TextField(
        value = email,
        onValueChange = {
          email = it
        },
        modifier = Modifier
          .padding(20.dp, 50.dp, 20.dp, 0.dp)
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
        leadingIcon = {
          Icon(
            imageVector = Icons.Filled.Email,
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
        value = senha,
        onValueChange = {
          senha = it
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
          IconButton(
            onClick = {
              senhaVisivel = !senhaVisivel
            },
          ) {
            val icon: Painter
            val customModifier: Modifier

            if(senhaVisivel) {
              icon = painterResource(R.drawable.olho_senha_vis_red)
              customModifier = Modifier.size(20.dp)
            } else {
              icon = painterResource(R.drawable.olho_senha_red)
              customModifier = Modifier.size(40.dp)
            }

            val description = if (senhaVisivel) "Ocultar Senha" else "Mostrar Senha"

            Icon(
              painter = icon,
              contentDescription = description,
              tint = Color.White,
              modifier = customModifier
            )
          }
        },
        modifier = Modifier
          .padding(20.dp, 20.dp, 20.dp, 50.dp)
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
        leadingIcon = {
          Icon(
            imageVector = Icons.Filled.Lock,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        },
        label = {
          Text(
            text = "Senha",
            color = Color.LightGray,
            fontSize = 12.sp
          )
        },
        singleLine = true
      )

      Button(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp, 0.dp, 20.dp, 0.dp),
        shape = Shapes().large,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
          containerColor = Color(0XFF246BFD),
          contentColor = Color.White
        ),
        onClick = {
          if (email.isEmpty() || senha.isEmpty()) {
            coroutineScope.launch {
              snackbarHostState.showSnackbar("Preencha todos os campos.",
                actionLabel = "Ok",
                duration = SnackbarDuration.Short)
            }
          } else if (senha.length < 6)  {
            coroutineScope.launch {
              snackbarHostState.showSnackbar("A senha deve conter no mínimo 6 caracteres.",
                actionLabel = "Ok",
                duration = SnackbarDuration.Short)
            }
          } else if (senha.length > 10) {
            coroutineScope.launch {
              snackbarHostState.showSnackbar("A senha deve conter no máximo 10 caracteres.",
                actionLabel = "Ok",
                duration = SnackbarDuration.Short)
            }
          } else {
            EmailAndPasswordAuth().signInWithEmailAndPassword(email, senha) { result ->
              if (result) {
                navController.navigate("home_screen"){
                  popUpTo("login_screen"){inclusive = true}
                }
              } else {
                coroutineScope.launch {
                  snackbarHostState.showSnackbar("Erro ao fazer login no usuário",
                    actionLabel = "Ok",
                    duration = SnackbarDuration.Short)
                }
              }
            }
          }
        }
      ) {
        Text(
          text = "Fazer login",
          fontSize = 16.sp,
          color = Color.White
        )
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(0.dp, 60.dp, 0.dp, 20.dp),
        Alignment.Center
      ) {
        Text(
          text = "Ou continue com",
          fontSize = 18.sp,
          color = Color.White
        )
      }

      Button(
        modifier = Modifier
          .fillMaxWidth()
          .height(45.dp)
          .padding(20.dp, 0.dp, 20.dp, 0.dp),
        shape = Shapes().large,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
          containerColor = Color(0XFF1F222A),
          contentColor = Color.White
        ),
        onClick = {}
      ) {
        Text(
          text = "Continuar com Google",
          fontSize = 16.sp,
          color = Color.White,
          modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
        )

        Image(
          painter = painterResource(id = R.drawable.google_icon),
          contentDescription = null,
          modifier = Modifier.size(20.dp)
        )
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(0.dp, 40.dp, 0.dp, 0.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Não tem uma conta?",
            fontSize = 14.sp,
            color = Color.White
          )

          Button(
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
              containerColor = Color.Transparent,
              contentColor = Color(0XFF246BFD)
            ),
            onClick = {
              navController.navigate("register_screen") {
                popUpTo("login_screen") { inclusive = true }
              }

            }
          ) {
            Text(
              text = "Cadastre-se",
              fontSize = 14.sp,
              color = Color(0XFF246BFD),
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

//@Preview
//@Composable
//fun UserLoginPreview() {
//  UserLogin()
//}