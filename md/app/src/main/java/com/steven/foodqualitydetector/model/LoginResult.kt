package com.steven.foodqualitydetector.model

data class LoginResult(
    val data: UserData?,
    val errorMessage: String?,
    val firebaseToken: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?,
)