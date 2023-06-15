package com.steven.foodqualitydetector.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Food(
    val id: String = "",
    val createdAt: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val labels: List<String> = emptyList(),
    val percentages: List<String> = emptyList(),
) {
}