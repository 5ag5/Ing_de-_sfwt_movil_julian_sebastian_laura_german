package com.tsdc.vinilos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tsdc.vinilos.ui.shared.components.VinilosNavBar

@Composable
fun CreateAlbumScreen(
    onBack: () -> Unit,
    onBottomNavSelected: (Int) -> Unit,
    onCreateAlbum: () -> Unit = {},
    onSaveDraft: () -> Unit = {}
) {
    var albumTitle by rememberSaveable { mutableStateOf("") }
    var releaseDate by rememberSaveable { mutableStateOf("") }
    var coverUrl by rememberSaveable { mutableStateOf("") }
    var genre by rememberSaveable { mutableStateOf("") }
    var recordLabel by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var isGenreExpanded by remember { mutableStateOf(false) }

    val genres = listOf(
        "Classical",
        "Salsa",
        "Rock",
        "Folk"
    )

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
                IconButton(onClick = onBack) {
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

            Text(
                text = "Create Album",
                color = Color(0xFF111111),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 36.sp
            )
            Text(
                text = "Add a new record to your collection",
                color = Color(0xFF7E8497),
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            CreateAlbumTextField(
                value = albumTitle,
                onValueChange = { albumTitle = it },
                placeholder = "Album Title"
            )

            Spacer(modifier = Modifier.height(12.dp))

            CreateAlbumTextField(
                value = releaseDate,
                onValueChange = { releaseDate = it },
                placeholder = "mm/dd/yyyy",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Event,
                        contentDescription = "Select release date",
                        tint = Color(0xFF111111)
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CreateAlbumTextField(
                value = coverUrl,
                onValueChange = { coverUrl = it },
                placeholder = "Cover URL"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = genre,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(text = "Genre", color = Color(0xFF6B7280))
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Open genre options",
                            tint = Color(0xFF6B7280)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isGenreExpanded = true },
                    shape = RoundedCornerShape(14.dp),
                    colors = createAlbumFieldColors()
                )

                DropdownMenu(
                    expanded = isGenreExpanded,
                    onDismissRequest = { isGenreExpanded = false }
                ) {
                    genres.forEach { genreOption ->
                        DropdownMenuItem(
                            text = { Text(text = genreOption) },
                            onClick = {
                                genre = genreOption
                                isGenreExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            CreateAlbumTextField(
                value = recordLabel,
                onValueChange = { recordLabel = it },
                placeholder = "Record Label"
            )

            Spacer(modifier = Modifier.height(12.dp))

            CreateAlbumTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = "Description",
                minLines = 4,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onCreateAlbum,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B35BD))
            ) {
                Text(
                    text = "Create Album",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(
                onClick = onSaveDraft,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF6B7280))
            ) {
                Text(
                    text = "Save Draft",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CreateAlbumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minLines: Int = 1,
    maxLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder, color = Color(0xFF6B7280))
        },
        trailingIcon = trailingIcon,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        minLines = minLines,
        maxLines = maxLines,
        colors = createAlbumFieldColors()
    )
}

@Composable
private fun createAlbumFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color(0xFFF4F5F8),
    unfocusedContainerColor = Color(0xFFF4F5F8),
    focusedBorderColor = Color(0xFF7F879B),
    unfocusedBorderColor = Color(0xFF9097AA),
    focusedTextColor = Color(0xFF1F2937),
    unfocusedTextColor = Color(0xFF1F2937)
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateAlbumScreenPreview() {
    CreateAlbumScreen(
        onBack = {},
        onBottomNavSelected = {}
    )
}
