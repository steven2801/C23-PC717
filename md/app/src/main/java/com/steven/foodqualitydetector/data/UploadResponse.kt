package com.steven.foodqualitydetector.data

import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(
    val id: String,
    val success: String
)