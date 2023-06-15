package com.steven.foodqualitydetector.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.steven.foodqualitydetector.R
import com.steven.foodqualitydetector.model.UserData

@Composable
fun ExpandedTopBar(userData: UserData?, onSignOut: () -> Unit) {
    val COLLAPSED_TOP_BAR_HEIGHT = 56.dp
    val EXPANDED_TOP_BAR_HEIGHT = 230.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(EXPANDED_TOP_BAR_HEIGHT - COLLAPSED_TOP_BAR_HEIGHT)
            ,
        contentAlignment = Alignment.BottomStart
    ) {
        Box(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.background
                    )
                ),
            ))
        Image(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(-1F),
            painter = painterResource(R.drawable.food),
            contentDescription = "background_image",
            contentScale = ContentScale.Crop
        )
//        Row(modifier = Modifier.fillMaxWidth().height(56.dp).padding(16.dp)) {
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(painter = painterResource(id = R.drawable.baseline_logout_24), contentDescription = "Logout")
//            }
//        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(EXPANDED_TOP_BAR_HEIGHT)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background)
                    ),
                ),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
                    ,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onSignOut,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_logout_24),
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            Column() {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .zIndex(1F),
                    text = "Welcome back,",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    modifier = Modifier
                        .padding(bottom = 16.dp, start = 16.dp)
                        .zIndex(1F),
                    text = "${userData?.username}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

        }
    }
}