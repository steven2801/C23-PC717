package com.steven.foodqualitydetector.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.steven.foodqualitydetector.data.Section

@Composable
fun InfoSection(section: Section) {
    val resource = section.resource
    val title = section.title
    val description = section.description
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = resource),
            contentDescription = "Illustration"
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(text = description)
    }
}