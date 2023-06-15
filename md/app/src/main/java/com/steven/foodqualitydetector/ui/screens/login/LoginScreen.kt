package com.steven.foodqualitydetector.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.steven.foodqualitydetector.data.SectionData.sections
import com.steven.foodqualitydetector.ui.components.Bullet
import com.steven.foodqualitydetector.ui.components.InfoSection

@Composable
fun LoginScreen(
    state: LoginState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.loginError) {
        state.loginError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(top = 32.dp, bottom = 24.dp, start = 8.dp, end = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            EndlessHorizontalPager(
                items = listOf("1", "2", "3", "4"),
            )
            Button(
                onClick = onSignInClick,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
            ) {
                Text(text = "Masuk dengan Google")
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EndlessHorizontalPager(
    items: List<String>,
) {
    val pageCount = Int.MAX_VALUE
    val pagerState = rememberPagerState(
        initialPage = pageCount / 4
    )

    val currentPage by remember() {
        derivedStateOf { items[pagerState.currentPage % items.size] }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Bullet(active = currentPage == "4")
        Bullet(active = currentPage == "1")
        Bullet(active = currentPage == "2")
        Bullet(active = currentPage == "3")
    }
    HorizontalPager(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, bottom = 16.dp),
        pageCount = pageCount,
        state = pagerState,
    ) { page ->
        when (items[page % items.size]) {
            "4" -> {
                InfoSection(section = sections[0])
            }

            "1" -> {
                InfoSection(section = sections[1])
            }

            "2" -> {
                InfoSection(section = sections[2])
            }

            "3" -> {
                InfoSection(section = sections[3])
            }
        }
    }
}