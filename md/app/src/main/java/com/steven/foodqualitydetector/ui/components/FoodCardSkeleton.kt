package com.steven.foodqualitydetector.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.steven.foodqualitydetector.ui.modifiers.shimmerEffect
import com.steven.foodqualitydetector.ui.theme.Shapes

@Composable
fun FoodCardSkeleton(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(12.dp)
                    .clip(shape = Shapes.small)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(12.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(shape = Shapes.medium)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .width(72.dp)
                .height(12.dp)
                .clip(shape = Shapes.small)
                .shimmerEffect()
        )
    }
}