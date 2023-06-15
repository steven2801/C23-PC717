package com.steven.foodqualitydetector.ui.screens.login

import androidx.lifecycle.ViewModel
import com.steven.foodqualitydetector.model.LoginResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onLoginResult(result: LoginResult) {
        _state.update { it.copy(
            isLoginSuccessful = result.data != null,
            loginError = result.errorMessage,
            firebaseToken = result.firebaseToken
        ) }
    }

    fun resetState() {
        _state.update { LoginState() }
    }
}