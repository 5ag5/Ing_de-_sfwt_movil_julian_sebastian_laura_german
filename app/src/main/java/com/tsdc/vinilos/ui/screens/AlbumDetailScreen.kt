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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tsdc.vinilos.ui.shared.theme.VinilosTheme

data class AlbumDetailUiModel(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val genre: String,
    val recordLabel: String,
    val description: String
)

private fun getMockDetail(albumId: Int): AlbumDetailUiModel {
    return AlbumDetailUiModel(
        id = albumId,
        name = "Buscando América",
        cover = "https://upload.wikimedia.org/wikipedia/en/6/67/Ruben_Blades_-_Buscando_America.jpg",
        releaseDate = "1984-01-01",
        genre = "Salsa",
        recordLabel = "Elektra",
        description = "Álbum de referencia para HU02. "
            + "Este contenido es temporal mientras se integra el endpoint de detalle."
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AlbumDetailScreenPreview() {
    VinilosTheme {
        AlbumDetailScreen(
            albumId = 101,
            onBack = {}
        )
    }
}
@Composable
fun AlbumDetailScreen(albumId: Int, onBack: () -> Unit) {
    val album = getMockDetail(albumId)

    Column(
        modifier = Modifier
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
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AsyncImage(
            model = album.cover,
            contentDescription = album.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.4f)
                .clip(RoundedCornerShape(16.dp))
        )

        Text(
            text = album.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A1A2E),
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFE1E4F2)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                DetailRow(label = "ID", value = album.id.toString())
                DetailRow(label = "Fecha de lanzamiento", value = album.releaseDate)
                DetailRow(label = "Género", value = album.genre)
                DetailRow(label = "Sello discográfico", value = album.recordLabel)
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
            text = album.description,
            fontSize = 15.sp,
            color = Color(0xFF4D4D4D),
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "ID seleccionado: $albumId",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2B35BD),
            modifier = Modifier.padding(bottom = 16.dp)
        )
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
