package com.steven.foodqualitydetector.ui.screens.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.steven.foodqualitydetector.R
import com.steven.foodqualitydetector.data.FoodRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.steven.foodqualitydetector.ui.components.DetailScreenSkeleton
import com.steven.foodqualitydetector.ui.screens.home.HomeViewModel
import com.steven.foodqualitydetector.utils.TimeFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenAfterCapture(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Boolean,
    viewModel: HomeViewModel,
    id: String,
    snackbarHost: @Composable () -> Unit,
    snackbarHostState: SnackbarHostState,
    isLoading: Boolean
) {

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isLoading) {
        if (!isLoading) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Gambar berhasil diproses",
                    actionLabel = "OK"
                )
            }
        }
    }
    if (isLoading || id.isNullOrEmpty()) {
        DetailScreenSkeleton(
            modifier = modifier,
            onBackPressed = onBackPressed,
            snackbarHost = snackbarHost,
            snackbarHostState = snackbarHostState
        )
    } else {
        Scaffold(
            snackbarHost = snackbarHost,
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Detail")
                    },
                    modifier = Modifier
                        .padding(all = 4.dp),
                    navigationIcon = {
                        IconButton(onClick = { onBackPressed() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(id = R.string.app_name)
                            )
                        }
                    }
                )
            },
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                val food = viewModel.getFoodById(id)
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp),
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item(1) {
                        Column(
                            modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = food.imageUrl,
                                contentDescription = food.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .aspectRatio(16f / 16f)
                                    .clip(shape = MaterialTheme.shapes.medium)
                            )
                        }
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = modifier

                                    .background(color = MaterialTheme.colorScheme.background),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_access_time_24),
                                    contentDescription = "Time",
                                    modifier = modifier.size(16.dp)
                                )
                                Text(text = TimeFormatter.formatIsoStringToRelativeTime(food.createdAt), style = MaterialTheme.typography.bodySmall)
                            }
                            Badge(
                                content = {
                                    Text(
                                        text = food.category,
                                        modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                },
                                containerColor = when (food.category) {
                                    "fresh" -> Color(0xFF39ad1c)
                                    "half-fresh" -> Color(0xFFcfa227)
                                    else -> Color(0xccb51d00)
                                }
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(4.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = food.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Medium)
                        }
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End) {
                            Text(
                                text = "Likelihood"
                            )
                        }
                    }

                    itemsIndexed(food.labels) { index, label ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = MaterialTheme.shapes.medium)
                                .background(color = if (index < 3) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onSecondary
                                })
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = label,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = food.percentages[index],
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}