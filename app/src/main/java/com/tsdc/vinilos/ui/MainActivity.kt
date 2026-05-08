package com.tsdc.vinilos.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tsdc.vinilos.di.AppModule
import com.tsdc.vinilos.ui.screens.AlbumDetailScreen
import com.tsdc.vinilos.ui.screens.HomeScreen
import com.tsdc.vinilos.ui.shared.theme.VinilosTheme
import com.tsdc.vinilos.ui.viewmodels.AlbumDetailViewModel
import com.tsdc.vinilos.ui.viewmodels.AlbumViewModel
import com.tsdc.vinilos.ui.viewmodels.ArtistViewModel
import com.tsdc.vinilos.ui.viewmodels.CollectorViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppModule.init(this)

        lifecycleScope.launch {
            try {
                val albums = AppModule.apiService.getAlbums()
                Log.d("VinilosTest", "Álbumes recibidos: $albums")
            } catch (e: Exception) {
                Log.e("VinilosTest", "Error llamando backend", e)
            }
        }
        setContent {
            VinilosTheme {
                val viewModel = AlbumViewModel(AppModule.getAlbumsUseCase)
                val artistViewModel = ArtistViewModel(AppModule.getArtistsUseCase)
                val collectorViewModel = CollectorViewModel(AppModule.getCollectorsUseCase)
                val detailViewModel = AlbumDetailViewModel(AppModule.getAlbumByIdUseCase)
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            albumViewModel = viewModel,
                            artistViewModel = artistViewModel,
                            collectorViewModel = collectorViewModel,
                            onAlbumClick = { albumId ->
                                navController.navigate("album_detail/$albumId")
                            }
                        )
                    }
                    composable("home_albums") {
                        HomeScreen(
                            albumViewModel = viewModel,
                            artistViewModel = artistViewModel,
                            collectorViewModel = collectorViewModel,
                            initialTab = 1,
                            onAlbumClick = { albumId ->
                                navController.navigate("album_detail/$albumId")
                            }
                        )
                    }
                    composable(
                        route = "album_detail/{albumId}",
                        arguments = listOf(navArgument("albumId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val albumId = backStackEntry.arguments?.getInt("albumId") ?: -1
                        AlbumDetailScreen(
                            viewModel = detailViewModel,
                            albumId = albumId,
                            onBack = {
                                navController.navigate("home_albums") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
