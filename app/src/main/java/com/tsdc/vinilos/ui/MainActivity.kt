package com.tsdc.vinilos.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tsdc.vinilos.di.AppModule
import com.tsdc.vinilos.ui.screens.AlbumDetailScreen
import com.tsdc.vinilos.ui.screens.ArtistDetailScreen
import com.tsdc.vinilos.ui.screens.CollectorDetailScreen
import com.tsdc.vinilos.ui.screens.CreateAlbumScreen
import com.tsdc.vinilos.ui.screens.HomeScreen
import com.tsdc.vinilos.ui.shared.theme.VinilosTheme
import com.tsdc.vinilos.ui.viewmodels.AlbumDetailViewModel
import com.tsdc.vinilos.ui.viewmodels.AlbumViewModel
import com.tsdc.vinilos.ui.viewmodels.ArtistDetailViewModel
import com.tsdc.vinilos.ui.viewmodels.ArtistViewModel
import com.tsdc.vinilos.ui.viewmodels.CollectorDetailViewModel
import com.tsdc.vinilos.ui.viewmodels.CollectorViewModel



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppModule.init(this)


        setContent {
            VinilosTheme {
                val viewModel = AlbumViewModel(AppModule.getAlbumsUseCase)
                val artistViewModel = ArtistViewModel(AppModule.getArtistsUseCase)
                val collectorViewModel = CollectorViewModel(AppModule.getCollectorsUseCase)
                val detailViewModel = AlbumDetailViewModel(AppModule.getAlbumByIdUseCase)
                val artistDetailViewModel = ArtistDetailViewModel(
                    AppModule.getArtistByIdUseCase,
                    AppModule.toggleFavoriteArtistUseCase,
                    AppModule.isFavoriteArtistUseCase
                )
                val collectorDetailViewModel = CollectorDetailViewModel(AppModule.getCollectorByIdUseCase)
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            albumViewModel = viewModel,
                            artistViewModel = artistViewModel,
                            collectorViewModel = collectorViewModel,
                            onCreateAlbumClick = {
                                navController.navigate("create_album")
                            },
                            onAlbumClick = { albumId ->
                                navController.navigate("album_detail/$albumId")
                            },
                            onArtistClick = { artistId ->
                                navController.navigate("artist_detail/$artistId")
                            },
                            onCollectorClick = { collectorId ->
                                navController.navigate("collector_detail/$collectorId")
                            }
                        )
                    }
                    composable("home_albums") {
                        HomeScreen(
                            albumViewModel = viewModel,
                            artistViewModel = artistViewModel,
                            collectorViewModel = collectorViewModel,
                            initialTab = 1,
                            onCreateAlbumClick = {
                                navController.navigate("create_album")
                            },
                            onAlbumClick = { albumId ->
                                navController.navigate("album_detail/$albumId")
                            },
                            onArtistClick = { artistId ->
                                navController.navigate("artist_detail/$artistId")
                            },
                            onCollectorClick = { collectorId ->
                                navController.navigate("collector_detail/$collectorId")
                            }
                        )
                    }
                    composable("home_artists") {
                        HomeScreen(
                            albumViewModel = viewModel,
                            artistViewModel = artistViewModel,
                            collectorViewModel = collectorViewModel,
                            initialTab = 2,
                            onCreateAlbumClick = {
                                navController.navigate("create_album")
                            },
                            onAlbumClick = { albumId ->
                                navController.navigate("album_detail/$albumId")
                            },
                            onArtistClick = { artistId ->
                                navController.navigate("artist_detail/$artistId")
                            },
                            onCollectorClick = { collectorId ->
                                navController.navigate("collector_detail/$collectorId")
                            }
                        )
                    }
                    composable("home_collectors") {
                        HomeScreen(
                            albumViewModel = viewModel,
                            artistViewModel = artistViewModel,
                            collectorViewModel = collectorViewModel,
                            initialTab = 3,
                            onAlbumClick = { albumId ->
                                navController.navigate("album_detail/$albumId")
                            },
                            onArtistClick = { artistId ->
                                navController.navigate("artist_detail/$artistId")
                            },
                            onCollectorClick = { collectorId ->
                                navController.navigate("collector_detail/$collectorId")
                            }
                        )
                    }
                    composable(
                        route = "create_album"
                    ) {
                        CreateAlbumScreen(
                            onBack = { navController.popBackStack() },
                            onBottomNavSelected = { index ->
                                when (index) {
                                    0 -> navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                    1 -> navController.navigate("home_albums") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                    2 -> navController.navigate("home_artists") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                    3 -> navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
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
                    composable(
                        route = "artist_detail/{artistId}",
                        arguments = listOf(navArgument("artistId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val artistId = backStackEntry.arguments?.getInt("artistId") ?: -1
                        ArtistDetailScreen(
                            viewModel = artistDetailViewModel,
                            artistId = artistId,
                            onBack = {
                                navController.navigate("home_artists") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(
                        route = "collector_detail/{collectorId}",
                        arguments = listOf(navArgument("collectorId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val collectorId = backStackEntry.arguments?.getInt("collectorId") ?: -1
                        CollectorDetailScreen(
                            viewModel = collectorDetailViewModel,
                            collectorId = collectorId,
                            onBack = {
                                navController.navigate("home_collectors") {
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
