package com.example.planup.ui.screens

import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planup.R
import com.example.planup.auth.FirebaseAuthManager
import com.google.firebase.FirebaseApp

@Composable
fun LoginScreen(navController: NavHostController) {
  var email by remember {
    mutableStateOf("")
  }

  var senha by remember {
    mutableStateOf("")
  }

  var check by remember {
    mutableStateOf(false)
  }

  Scaffold(
    modifier = Modifier.fillMaxSize()
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
        visualTransformation = PasswordVisualTransformation(),
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
        leadingIcon = {
          Icon(
            imageVector = Icons.Filled.Lock,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        },
        trailingIcon = {
          Image(
            painter = painterResource(id = R.drawable.olho_senha_red),
            contentDescription = null,
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White),
            modifier = Modifier.size(35.dp)
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

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(0.dp, 20.dp, 20.dp, 0.dp),
        horizontalArrangement = Arrangement.Center
      ) {
        Checkbox(
          checked = check,
          onCheckedChange = {
            check = it
          },
          colors = androidx.compose.material3.CheckboxDefaults.colors(
            checkedColor = Color(0XFF246BFD),
            uncheckedColor = Color(0XFF246BFD)
          )
        )

        Text(
          text = "Lembrar de mim",
          color = Color.White,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp)
        )
      }

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
            Log.d("Button", "Preencha todos os campos.")
          } else if (senha.length < 6)  {
            Log.d("Button", "A senha deve conter no mínimo 6 caracteres.")
          } else if (senha.length > 10) {
            Log.d("Button", "A senha deve conter no máximo 10 caracteres.")
          } else {
            FirebaseAuthManager().signInWithEmailAndPassword(email, senha) { result ->
              Log.d("Button", "Dados salvos com sucesso: $result")

              if (result) {
                navController.navigate("home_screen"){
                  popUpTo("login_screen"){inclusive = true}
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
          .padding(0.dp, 60.dp, 0.dp, 0.dp),
        Alignment.Center
      ) {
        Text(
          text = "Ou continue com",
          fontSize = 18.sp,
          color = Color.White
        )
      }

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(40.dp, 30.dp, 40.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        Button(
          modifier = Modifier
            .width(80.dp)
            .height(45.dp)
            .padding(0.dp, 0.dp, 0.dp, 0.dp),
          shape = Shapes().small,
          colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Color(0XFF1F222A),
            contentColor = Color.White
          ),
          onClick = { /*TODO*/ }
        ) {
          Image(
            painter = painterResource(id = R.drawable.facebook_icon),
            contentDescription = null,
            modifier = Modifier.size(35.dp)
          )
        }

        Button(
          modifier = Modifier
            .width(80.dp)
            .height(45.dp)
            .padding(0.dp, 0.dp, 0.dp, 0.dp),
          shape = Shapes().small,
          colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Color(0XFF1F222A),
            contentColor = Color.White
          ),
          onClick = { /*TODO*/ }
        ) {
          Image(
            painter = painterResource(id = R.drawable.google_icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
          )
        }

        Button(
          modifier = Modifier
            .width(80.dp)
            .height(45.dp)
            .padding(0.dp, 0.dp, 0.dp, 0.dp),
          shape = Shapes().small,
          colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Color(0XFF1F222A),
            contentColor = Color.White
          ),
          onClick = { /*TODO*/ }
        ) {
          Image(
            painter = painterResource(id = R.drawable.apple_icon),
            contentDescription = null,
            modifier = Modifier.size(35.dp)
          )
        }

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