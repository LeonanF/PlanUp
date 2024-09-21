package com.example.planup.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseDatabaseManager {
  private val db = FirebaseFirestore.getInstance()

  fun saveUserData(userId: String, email: String, senha: String, check: Boolean): Boolean{
    var result = false
    val userData = hashMapOf(
      "email" to email,
      "senha" to senha,
      "check" to check
    )

    db.collection("users")
      .document(userId)
      .set(userData)
      .addOnCompleteListener {
        if (it.isSuccessful) {
          Log.d("Firebase", "Dados salvos com sucesso!")
          result = true
        }
      }
      .addOnFailureListener {
        Log.d("Firebase", "Erro ao salvar dados: ${it.message}")
        result = false
      }
    return result
  }

  fun getUserData(userId: String): Map<String, Any>? {
    val documentSnapshot = db.collection("users")
      .document(userId)
      .get()
      .addOnSuccessListener {
        Log.d("Firebase", "Dados recuperados com sucesso!")
      }
      .addOnFailureListener {
        Log.d("Firebase", "Erro ao recuperar dados: ${it.message}")
      }
    return documentSnapshot.result.data
  }
}