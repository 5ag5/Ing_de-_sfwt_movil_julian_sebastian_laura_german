package com.tsdc.vinilos.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tsdc.vinilos.di.AppModule
import com.tsdc.vinilos.ui.viewmodels.AlbumViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        viewModel = AlbumViewModel(AppModule.getAlbumsUseCase),
        onAlbumClick = {}
    )
}

@Composable
fun HomeScreen(
    viewModel: AlbumViewModel,
    initialTab: Int = 0,
    onAlbumClick: (Int) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(initialTab) }

    val navLabels = listOf("Home", "Albums", "Artists", "Collectors")
    val navIcons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Star,
        Icons.Filled.Person,
        Icons.Filled.Favorite
    )

    Scaffold(
        floatingActionButton = {
            if (selectedIndex == 1) {
                FloatingActionButton(
                    onClick = { },
                    containerColor = Color(0xFF2B35BD),
                    shape = CircleShape,
                    modifier = Modifier.size(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar album",
                        tint = Color.White
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                navLabels.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(
                                imageVector = navIcons[index],
                                contentDescription = label
                            )
                        },
                        label = {
                            Text(
                                text = label.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2B35BD),
                            selectedTextColor = Color(0xFF2B35BD),
                            indicatorColor = Color(0xFFE8EAFF),
                            unselectedIconColor = Color(0xFF888888),
                            unselectedTextColor = Color(0xFF888888)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (selectedIndex) {
                0 -> WelcomeScreen()
                1 -> AlbumScreen(
                    viewModel = viewModel,
                    onAlbumClick = onAlbumClick
                )
                2 -> PlaceholderScreen("Artists")
                3 -> PlaceholderScreen("Collectors")
            }
        }
    }
}

@Composable
private fun WelcomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Bienvenido a Vinilos",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A2E)
        )
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "$name\nProximamente",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF888888)
        )
    }
}
