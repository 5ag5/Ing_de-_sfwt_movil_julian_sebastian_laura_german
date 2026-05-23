package com.tsdc.vinilos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.models.Track
import com.tsdc.vinilos.ui.shared.components.VinilosNavBar
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import com.tsdc.vinilos.ui.viewmodels.AlbumTracksViewModel

@Composable
fun AlbumTracksScreen(
    viewModel: AlbumTracksViewModel,
    albumId: Int,
    onBack: () -> Unit,
    onBottomNavSelected: (Int) -> Unit,
    onSaveTracklist: () -> Unit = onBack
) {
    val album by viewModel.album.collectAsState()
    val tracks by viewModel.tracks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(albumId) {
        viewModel.load(albumId)
    }

    AlbumTracksContent(
        album = album,
        tracks = tracks,
        isLoading = isLoading,
        error = error,
        onBack = onBack,
        onAddTrack = { name, duration ->
            viewModel.addTrack(albumId, name, duration)
        },
        onSaveTracklist = onSaveTracklist,
        onBottomNavSelected = onBottomNavSelected
    )
}

@Composable
private fun AlbumTracksContent(
    album: Album?,
    tracks: List<Track>,
    isLoading: Boolean,
    error: String?,
    onBack: () -> Unit,
    onAddTrack: (String, String) -> Unit,
    onSaveTracklist: () -> Unit,
    onBottomNavSelected: (Int) -> Unit
) {
    var trackTitle by remember { mutableStateOf("") }
    var trackDuration by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFFF2F2F6),
        bottomBar = {
            VinilosNavBar(
                selectedIndex = 1,
                onItemSelected = onBottomNavSelected
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .testTag(UiTestTags.ALBUM_TRACKS_ROOT)
                .fillMaxSize()
                .background(Color(0xFFF2F2F6))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag(UiTestTags.ALBUM_TRACKS_BACK_BUTTON)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF2B35BD)
                    )
                }
                Text(
                    text = "Albums",
                    color = Color(0xFF111111),
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            AlbumHeader(album = album)

            Spacer(modifier = Modifier.height(18.dp))

            if (error != null) {
                Text(
                    text = error,
                    color = Color(0xFFB3261E),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFF4)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    AlbumTracksTextField(
                        value = trackTitle,
                        onValueChange = { trackTitle = it },
                        placeholder = "Track Title",
                        modifier = Modifier.testTag(UiTestTags.ALBUM_TRACKS_TITLE_FIELD)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AlbumTracksTextField(
                        value = trackDuration,
                        onValueChange = { trackDuration = it },
                        placeholder = "Duration 00:00",
                        modifier = Modifier.testTag(UiTestTags.ALBUM_TRACKS_DURATION_FIELD)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (trackTitle.isNotBlank() && trackDuration.isNotBlank()) {
                                onAddTrack(trackTitle.trim(), trackDuration.trim())
                                trackTitle = ""
                                trackDuration = ""
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .testTag(UiTestTags.ALBUM_TRACKS_ADD_BUTTON)
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B35BD))
                    ) {
                        Text(
                            text = "Add Track",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "TRACKLIST",
                color = Color(0xFF7E8497),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (isLoading && tracks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF2B35BD))
                }
            } else {
                TracksList(tracks = tracks)
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onSaveTracklist,
                enabled = !isLoading,
                modifier = Modifier
                    .testTag(UiTestTags.ALBUM_TRACKS_SAVE_BUTTON)
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111111))
            ) {
                Text(
                    text = "Save Tracklist",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AlbumHeader(album: Album?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE5E5EA))
        ) {
            if (!album?.cover.isNullOrBlank()) {
                AsyncImage(
                    model = album?.cover,
                    contentDescription = album?.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        Column {
            Text(
                text = "EDITING",
                color = Color(0xFF7E8497),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = album?.name ?: "—",
                color = Color(0xFF111111),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun TracksList(tracks: List<Track>) {
    if (tracks.isEmpty()) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFF4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Aún no hay tracks. Agrega el primero con el formulario.",
                color = Color(0xFF7E8497),
                fontSize = 14.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .testTag(UiTestTags.ALBUM_TRACKS_LIST)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tracks.forEach { track ->
            TrackRow(track = track)
        }
    }
}

@Composable
private fun TrackRow(track: Track) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFF4)),
        modifier = Modifier
            .testTag(UiTestTags.ALBUM_TRACKS_LIST_ITEM)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = track.name,
                color = Color(0xFF111111),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = track.duration,
                color = Color(0xFF111111),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun AlbumTracksTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder, color = Color(0xFF6B7280))
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color(0xFF7F879B),
            unfocusedBorderColor = Color(0xFFCFD3DD),
            focusedTextColor = Color(0xFF1F2937),
            unfocusedTextColor = Color(0xFF1F2937)
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AlbumTracksScreenPreview() {
    AlbumTracksContent(
        album = Album(
            id = 101,
            name = "Hotel California",
            cover = "https://upload.wikimedia.org/wikipedia/en/4/49/Hotelcalifornia.jpg",
            releaseDate = "1976-12-08",
            description = "Quinto álbum de estudio de Eagles.",
            genre = "Rock",
            recordLabel = "Elektra"
        ),
        tracks = listOf(
            Track(id = 1, name = "New Kid in Town", duration = "03:45"),
            Track(id = 2, name = "Hotel California", duration = "04:12")
        ),
        isLoading = false,
        error = null,
        onBack = {},
        onAddTrack = { _, _ -> },
        onSaveTracklist = {},
        onBottomNavSelected = {}
    )
}
