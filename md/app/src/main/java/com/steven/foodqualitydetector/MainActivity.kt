package com.steven.foodqualitydetector

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.steven.foodqualitydetector.model.GoogleAuthUIClient
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.steven.foodqualitydetector.ui.theme.FoodDetectiveTheme
import com.google.android.gms.auth.api.identity.Identity
import com.steven.foodqualitydetector.data.FoodRepository
import com.steven.foodqualitydetector.navigation.Screen
import com.steven.foodqualitydetector.ui.screens.detail.DetailScreen
import com.steven.foodqualitydetector.ui.screens.detail.DetailScreenAfterCapture
import com.steven.foodqualitydetector.ui.screens.detail.ViewModelFactory
import com.steven.foodqualitydetector.ui.screens.home.HomeScreen
import com.steven.foodqualitydetector.ui.screens.home.HomeViewModel
import com.steven.foodqualitydetector.ui.screens.initial.InitialScreen
import com.steven.foodqualitydetector.ui.screens.login.LoginScreen
import com.steven.foodqualitydetector.ui.screens.login.LoginViewModel
import com.steven.foodqualitydetector.ui.screens.scan_result.ScanResultScreen
import com.steven.foodqualitydetector.utils.PhotoUriManager
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val googleAuthUIClient by lazy {
        GoogleAuthUIClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodDetectiveTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        val navController = rememberNavController()

                        val homeViewModel: HomeViewModel = viewModel(
                            factory = com.steven.foodqualitydetector.ui.screens.home.ViewModelFactory(
                                FoodRepository()
                            )
                        )

                        val viewModel = viewModel<LoginViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        val photoUriManager = PhotoUriManager(LocalContext.current)
                        val photoViewModel: PhotoViewModel = viewModel(
                            factory = PhotoViewModelFactory(photoUriManager)
                        )

                        val isLoading by photoViewModel.isLoading
                        val id by photoViewModel.id

                        val snackbarHostState = remember { SnackbarHostState() }
                        val scope = rememberCoroutineScope()

                        NavHost(
                            navController = navController,
                            startDestination = Screen.Initial.route
                        ) {
                            composable(Screen.Initial.route) {
                                LaunchedEffect(key1 = Unit) {
                                    val user = googleAuthUIClient.getSignedInUser()
                                    if (user != null) {
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Initial.route) {
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        navController.navigate(Screen.Login.route) {
                                            popUpTo(Screen.Initial.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }

                                InitialScreen()
                            }
                            composable(Screen.Login.route) {
                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = { result ->
                                        if (result.resultCode == RESULT_OK) {
                                            lifecycleScope.launch {
                                                val signInResult =
                                                    googleAuthUIClient.signInWithIntent(
                                                        intent = result.data ?: return@launch
                                                    )

                                                viewModel.onLoginResult(signInResult)
                                            }
                                        }
                                    }
                                )

                                LaunchedEffect(key1 = state.isLoginSuccessful) {
                                    if (state.isLoginSuccessful) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Selamat datang di aplikasi.",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Login.route) {
                                                inclusive = true
                                            }
                                        }

                                        viewModel.resetState()
                                    }
                                }

                                LoginScreen(
                                    state = state,
                                    onSignInClick = {
                                        lifecycleScope.launch {
                                            val signInIntentSender = googleAuthUIClient.signIn()
                                            launcher.launch(
                                                IntentSenderRequest.Builder(
                                                    signInIntentSender ?: return@launch
                                                ).build()
                                            )
                                        }
                                    }
                                )
                            }
                            composable(Screen.Home.route) {
                                HomeScreen(
                                    userData = googleAuthUIClient.getSignedInUser(),
                                    onSignOut = {
                                        lifecycleScope.launch {
                                            googleAuthUIClient.signOut()
                                            Toast.makeText(
                                                applicationContext,
                                                "Sampai jumpa kembali!",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            navController.navigate(Screen.Login.route) {
                                                popUpTo(Screen.Home.route) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    },
                                    navController = navController,
                                    viewModel = homeViewModel,
                                    photoViewModel = photoViewModel,
                                    photoUriManager = photoUriManager
                                )
                            }
                            composable(
                                route = Screen.Detail.route,
                                arguments = listOf(navArgument("id") { type = NavType.StringType }),
                            ) {
                                val id = it.arguments?.getString("id") ?: ""
                                DetailScreen(
                                    modifier = Modifier,
                                    viewModel = homeViewModel,
                                    id = id,
                                    onBackPressed = {
                                        navController.popBackStack()
                                    },
                                    snackbarHost = { SnackbarHost(snackbarHostState) },
                                )
                            }
                            composable(
                                route = Screen.DetailAfterCapture.route,
                            ) {
                                DetailScreenAfterCapture(
                                    modifier = Modifier,
                                    viewModel = homeViewModel,
                                    id = id,
                                    onBackPressed = {
                                        navController.popBackStack()
                                    },
                                    snackbarHost = { SnackbarHost(snackbarHostState) },
                                    isLoading = isLoading,
                                    snackbarHostState = snackbarHostState
                                )
                            }
                            composable(
                                route = Screen.ScanResult.route,
                            ) {
                                ScanResultScreen(
                                    viewModel = photoViewModel,
                                    onBackPressed = {
                                        navController.popBackStack()
                                    },
                                    onSubmit = {
                                        scope.launch {
                                            googleAuthUIClient.getFirebaseToken().let { token ->
                                                if (token != null) {
                                                    photoViewModel.submit(this, applicationContext, token)
                                                }
                                            }
                                        }
                                        navController.navigate(Screen.DetailAfterCapture.route) {
                                            popUpTo(Screen.ScanResult.route) {
                                                inclusive = true
                                            }
                                        }
                                    },
                                    snackbarHost = { SnackbarHost(snackbarHostState) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


