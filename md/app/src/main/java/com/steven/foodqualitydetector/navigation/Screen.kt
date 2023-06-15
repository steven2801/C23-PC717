package com.steven.foodqualitydetector.navigation

sealed class Screen(val route: String) {
    object Initial : Screen("initial")
    object Login : Screen("login")
    object Home : Screen("home")
    object ScanResult : Screen("scan_result")
    object Detail : Screen("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }
    object DetailAfterCapture : Screen("detail_after_capture")
}