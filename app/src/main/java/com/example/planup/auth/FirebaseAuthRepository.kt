package com.example.planup.auth

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthManager {
  private val auth = FirebaseAuth.getInstance()

  fun signUpWithEmailAndPassword(email: String, password: String, callback: (Boolean) -> Unit) {
    auth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener { register ->
        if (register.isSuccessful) {
          callback(true)
        } else {
          callback(false)
        }
      }.addOnFailureListener {
        callback(false)
      }
  }

  fun signInWithEmailAndPassword(email: String, password: String, callback: (Boolean) -> Unit) {
    auth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener { login ->
        if (login.isSuccessful) {
          callback(true)
        } else {
          callback(false)
        }
      }.addOnFailureListener {
        callback(false)
      }
  }
}