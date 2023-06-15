package com.steven.foodqualitydetector.ui.screens.login

data class LoginState(
    val isLoginSuccessful: Boolean = false,
    val loginError: String? = null,
    val firebaseToken: String? = null
)