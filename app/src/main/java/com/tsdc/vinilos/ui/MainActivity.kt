package com.tsdc.vinilos.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tsdc.vinilos.di.AppModule
import com.tsdc.vinilos.ui.screens.HomeScreen
import com.tsdc.vinilos.ui.shared.theme.VinilosTheme
import com.tsdc.vinilos.ui.viewmodels.AlbumViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                HomeScreen(viewModel)
            }
        }

    }
}
