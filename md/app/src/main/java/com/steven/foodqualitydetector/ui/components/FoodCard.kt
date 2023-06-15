package com.steven.foodqualitydetector.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.steven.foodqualitydetector.R
import com.steven.foodqualitydetector.data.Food
import com.steven.foodqualitydetector.navigation.Screen
import com.steven.foodqualitydetector.ui.theme.FoodDetectiveTheme
import com.steven.foodqualitydetector.utils.TimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodCard(food: Food, modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    val localFocusManager = LocalFocusManager.current
    Column(modifier = modifier.clickable {
        localFocusManager.clearFocus()
        navController.navigate(Screen.Detail.createRoute(food.id))
    }) {
        FoodCardHeader(name = food.title, type = food.category, modifier = modifier)
        FoodImage(
            contentDescription = food.title,
            modifier = modifier,
            url = food.imageUrl
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
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
    }
}

@Composable
fun FoodImage(url: String, contentDescription: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(16.dp)
                .aspectRatio(16f / 9f)
                .clip(shape = MaterialTheme.shapes.medium)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodCardHeader(name: String, type: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$name",
            modifier = modifier,
            style = MaterialTheme.typography.bodyMedium
        )
        Badge(
            content = {
                Text(
                    text = type,
                    modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            containerColor = when (type) {
                "fresh" -> Color(0xFF39ad1c)
                "half-fresh" -> Color(0xFFcfa227)
                else -> Color(0xccb51d00)
            }
        )
    }
}