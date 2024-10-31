package com.example.planup.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class EmailAndPasswordAuth {
  private val auth = FirebaseAuth.getInstance()
  private val currentUser = auth.currentUser

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

  fun getCurrentUser(): FirebaseUser? {
    return currentUser
  }

  fun signOutEmailAndPassword() {
    auth.signOut()
  }
}