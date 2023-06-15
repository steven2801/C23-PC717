package com.steven.foodqualitydetector.ui.screens.home

import android.net.Uri
import android.os.Build
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.steven.foodqualitydetector.PhotoViewModel
import com.steven.foodqualitydetector.R
import com.steven.foodqualitydetector.data.Food
import com.steven.foodqualitydetector.data.FoodRepository
import com.steven.foodqualitydetector.model.UserData
import com.steven.foodqualitydetector.navigation.Screen
import com.steven.foodqualitydetector.ui.components.ExpandedTopBar
import com.steven.foodqualitydetector.ui.components.FoodCard
import com.steven.foodqualitydetector.ui.components.FoodCardSkeleton
import com.steven.foodqualitydetector.ui.components.SearchBar
import com.steven.foodqualitydetector.utils.PhotoUriManager

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userData: UserData?,
    onSignOut: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory(FoodRepository())),
    photoViewModel: PhotoViewModel,
    photoUriManager: PhotoUriManager,
    navController: NavHostController = rememberNavController()
) {
    val query by viewModel.query
    val isRequestonGoing by photoViewModel.isLoading

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getFoods(userData!!.userId)
                HomeContent(
                    userData = userData,
                    onSignOut = onSignOut,
                    query = query,
                    searchFn = viewModel::search,
                    navController = navController,
                    isLoading = true,
                    modifier = modifier,
                    onPhotoTaken = photoViewModel::onImageCaptureResponse,
                    photoUriManager = photoUriManager,
                    isRequestOngoing = isRequestonGoing
                )
            }

            is UiState.Success -> {
                HomeContent(
                    userData = userData,
                    onSignOut = onSignOut,
                    foods = uiState.data,
                    query = query,
                    searchFn = viewModel::search,
                    navController = navController,
                    isLoading = false,
                    modifier = modifier,
                    onPhotoTaken = photoViewModel::onImageCaptureResponse,
                    photoUriManager = photoUriManager,
                    isRequestOngoing = isRequestonGoing
                )
            }

            is UiState.Error -> {

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit,
    userData: UserData?,
    foods: List<Food> = listOf(),
    query: String,
    searchFn: (String) -> Unit,
    isLoading: Boolean,
    isRequestOngoing: Boolean,
    navController: NavHostController = rememberNavController(),
    onPhotoTaken: (Uri) -> Unit,
    photoUriManager: PhotoUriManager,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val localFocusManager = LocalFocusManager.current

    var newImageUri: Uri? = null

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onPhotoTaken(newImageUri!!)
                navController.navigate(Screen.ScanResult.route)
            }
        }
    )

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {
                newImageUri = photoUriManager.buildNewUri()
                cameraLauncher.launch(newImageUri)
            }, modifier = Modifier
                .offset(x = (-8).dp)
                .clip(shape = RoundedCornerShape(50))
        ) {
            /* FAB content */
            Icon(
                painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                contentDescription = "Scan"
            )
        }
    }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp),
            modifier = Modifier
                .padding(padding)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        localFocusManager.clearFocus()
                    })
                }
        ) {
            item { ExpandedTopBar(userData = userData, onSignOut = onSignOut) }
            stickyHeader {
                Row(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme
                                .background
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    SearchBar(query = query, onQueryChange = searchFn)
                }
                Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                Spacer(modifier = modifier.height(16.dp))
            }

            if (isRequestOngoing) {
                item(1) {
                    Card(
                        shape = MaterialTheme.shapes.medium,
                        elevation = if (isDarkTheme) {
                            CardDefaults.cardElevation(defaultElevation = 4.dp)
                        } else {
                            CardDefaults.cardElevation(defaultElevation = 2.dp)
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        border = if (isDarkTheme) {
                            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        } else {
                            null
                        }
                    ) {
                        FoodCardSkeleton()
                    }
                    Spacer(modifier = modifier.height(16.dp))
                }
            }

            if (isLoading) {
                items(3) {
                    Card(
                        shape = MaterialTheme.shapes.medium,
                        elevation = if (isDarkTheme) {
                            CardDefaults.cardElevation(defaultElevation = 4.dp)
                        } else {
                            CardDefaults.cardElevation(defaultElevation = 2.dp)
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        border = if (isDarkTheme) {
                            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        } else {
                            null
                        }
                    ) {
                        FoodCardSkeleton()
                    }
                    Spacer(modifier = modifier.height(16.dp))
                }
            } else if (foods.isEmpty()) {
                items(1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .padding(start = 16.dp, end = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.Text(
                            text = "Your history is empty.",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            } else {
                items(foods, key = { it.id }) { food ->
                    Card(
                        shape = MaterialTheme.shapes.medium,
                        elevation = if (isDarkTheme) {
                            CardDefaults.cardElevation(defaultElevation = 4.dp)
                        } else {
                            CardDefaults.cardElevation(defaultElevation = 2.dp)
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        border = if (isDarkTheme) {
                            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        } else {
                            null
                        }
                    ) {
                        FoodCard(food, navController = navController)
                    }

                    Spacer(modifier = modifier.height(16.dp))
                }
            }
        }
    }
}