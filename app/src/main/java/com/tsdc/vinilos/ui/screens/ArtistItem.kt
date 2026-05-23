package com.tsdc.vinilos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tsdc.vinilos.domain.models.Artist
import com.tsdc.vinilos.ui.shared.constants.ColorConstants
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun ArtistItemPreview() {
    ArtistItem(
        artist = Artist(
            id = 1,
            name = "Miles Davis",
            image = "",
            description = "Jazz trumpeter and composer.",
            birthDate = Date()
        ),
        onClick = {}
    )
}

private fun artistItemContentDescription(artist: Artist, year: String): String =
    "Artista ${artist.name}, año de nacimiento $year"

@Composable
fun ArtistItem(artist: Artist, onClick: () -> Unit = {}) {
    val year = artist.birthDate.toArtistBadge()
    val accessibilityLabel = artistItemContentDescription(artist, year)
    Card(
        modifier = Modifier
            .testTag(UiTestTags.ARTIST_LIST_ITEM)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = accessibilityLabel
            }
            .clickable { onClick() },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = artist.image,
                contentDescription = null,
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f)
            ) {
                Text(
                    text = artist.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191B24)
                )
                Text(
                    text = artist.description
                        .replace("\n", " ")
                        .take(36)
                        .let { if (artist.description.length > 36) "$it..." else it },
                    fontSize = 12.sp,
                    color = ColorConstants.navUnselected,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

            Text(
                text = year,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ColorConstants.accentGoldAccessible
            )
        }
    }
}

private fun Date.toArtistBadge(): String {
    val formatter = SimpleDateFormat("yyyy", Locale.getDefault())
    return formatter.format(this)
}
