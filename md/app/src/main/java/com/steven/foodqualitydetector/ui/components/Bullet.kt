package com.steven.foodqualitydetector.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun Bullet(active: Boolean) {
    Text(
        text = "",
        modifier = Modifier
            .clip(shape = RoundedCornerShape(50))
            .background(
                if (active) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.secondaryContainer
                }
            )
            .padding(vertical = 2.dp, horizontal = 4.dp)
            .height(4.dp),
        color = MaterialTheme.colorScheme.onSecondaryContainer,
    )
}