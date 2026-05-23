package com.tsdc.vinilos.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.ui.shared.theme.VinilosTheme
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import com.tsdc.vinilos.ui.viewmodels.AlbumDetailViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AlbumDetailScreenPreview() {
    VinilosTheme {
        AlbumDetailContent(
            album = Album(
                id = 101,
                name = "Buscando América",
                cover = "https://upload.wikimedia.org/wikipedia/en/6/67/Ruben_Blades_-_Buscando_America.jpg",
                releaseDate = "1984-01-01",
                description = "Álbum de referencia para HU02 en modo preview.",
                genre = "Salsa",
                recordLabel = "Elektra"
            ),
            isLoading = false,
            error = null,
            onRetry = {},
            onBack = {}
        )
    }
}

@Composable
fun AlbumDetailScreen(
    viewModel: AlbumDetailViewModel,
    albumId: Int,
    onBack: () -> Unit
) {
    val album by viewModel.album.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(albumId) {
        viewModel.loadAlbum(albumId)
    }

    AlbumDetailContent(
        album = album,
        isLoading = isLoading,
        error = error,
        onRetry = { viewModel.loadAlbum(albumId) },
        onBack = onBack
    )
}

@Composable
private fun AlbumDetailContent(
    album: Album?,
    isLoading: Boolean,
    error: String?,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .testTag(UiTestTags.ALBUM_DETAIL_ROOT)
            .fillMaxSize()
            .background(Color(0xFFF2F2F6))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Detalle del álbum",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A1A2E),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .semantics { heading() }
        )

        if (isLoading) {
            CircularProgressIndicator(color = Color(0xFF2B35BD))
            Spacer(modifier = Modifier.height(20.dp))
        }

        if (error != null) {
            Text(
                text = error,
                fontSize = 14.sp,
                color = Color(0xFFB00020),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B35BD)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Reintentar", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        val currentAlbum = album ?: Album(
            id = -1,
            name = "Cargando...",
            cover = "",
            releaseDate = "-",
            description = "Esperando datos del backend.",
            genre = "-",
            recordLabel = "-"
        )

        Column(
            modifier = Modifier.semantics(mergeDescendants = true) {
                contentDescription = "Álbum ${currentAlbum.name}"
            }
        ) {
            AsyncImage(
                model = currentAlbum.cover,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.4f)
                    .clip(RoundedCornerShape(16.dp))
            )

            Text(
                text = currentAlbum.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A1A2E),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        Card(
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFE1E4F2)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                DetailRow(label = "ID", value = currentAlbum.id.toString())
                DetailRow(label = "Fecha de lanzamiento", value = currentAlbum.releaseDate)
                DetailRow(label = "Género", value = currentAlbum.genre)
                DetailRow(label = "Sello discográfico", value = currentAlbum.recordLabel)
            }
        }

        Text(
            text = "Descripción",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A2E),
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Text(
            text = currentAlbum.description,
            fontSize = 15.sp,
            color = Color(0xFF4D4D4D),
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B35BD)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver", color = Color.White)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Text(
        text = label,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF666666)
    )
    Text(
        text = value,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF1A1A2E),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
