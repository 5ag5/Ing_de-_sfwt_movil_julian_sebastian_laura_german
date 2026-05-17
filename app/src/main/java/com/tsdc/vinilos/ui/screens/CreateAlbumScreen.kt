package com.tsdc.vinilos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.models.NewAlbum
import com.tsdc.vinilos.domain.repositories.AlbumRepository
import com.tsdc.vinilos.domain.usecases.CreateAlbumUseCase
import com.tsdc.vinilos.ui.shared.components.VinilosNavBar
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import com.tsdc.vinilos.ui.viewmodels.CreateAlbumUiState
import com.tsdc.vinilos.ui.viewmodels.CreateAlbumViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlbumScreen(
    viewModel: CreateAlbumViewModel,
    onBack: () -> Unit,
    onBottomNavSelected: (Int) -> Unit,
    onCreateSuccess: () -> Unit = {},
    onSaveDraft: () -> Unit = { viewModel.resetForm() }
) {
    val state by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    val initialMillis = remember(state.releaseDate) {
        if (state.releaseDate.isBlank()) {
            System.currentTimeMillis()
        } else {
            runCatching {
                SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(state.releaseDate)?.time
            }.getOrNull() ?: System.currentTimeMillis()
        }
    }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            viewModel.onReleaseDateMillis(millis)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

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
                .testTag(UiTestTags.CREATE_ALBUM_ROOT)
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
                    modifier = Modifier.testTag(UiTestTags.CREATE_ALBUM_BACK_BUTTON)
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
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                placeholder = "Album Title",
                modifier = Modifier.testTag(UiTestTags.CREATE_ALBUM_TITLE_FIELD)
            )

            Spacer(modifier = Modifier.height(12.dp))

            CreateAlbumTextField(
                value = state.releaseDate,
                onValueChange = {},
                placeholder = "mm/dd/yyyy",
                modifier = Modifier
                    .testTag(UiTestTags.CREATE_ALBUM_DATE_FIELD)
                    .clickable { showDatePicker = true },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Select release date",
                            tint = Color(0xFF111111)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CreateAlbumTextField(
                value = state.coverUrl,
                onValueChange = viewModel::onCoverUrlChange,
                placeholder = "Cover URL",
                modifier = Modifier.testTag(UiTestTags.CREATE_ALBUM_COVER_FIELD)
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = state.isGenreMenuExpanded,
                onExpandedChange = { viewModel.onGenreMenuExpanded(it) },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.genre,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(text = "Genre", color = Color(0xFF6B7280))
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isGenreMenuExpanded)
                    },
                    modifier = Modifier
                        .testTag(UiTestTags.CREATE_ALBUM_GENRE_FIELD)
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = createAlbumFieldColors()
                )

                DropdownMenu(
                    expanded = state.isGenreMenuExpanded,
                    onDismissRequest = { viewModel.onGenreMenuExpanded(false) }
                ) {
                    CreateAlbumUiState.genres.forEach { genreOption ->
                        DropdownMenuItem(
                            text = { Text(text = genreOption) },
                            onClick = { viewModel.onGenreSelected(genreOption) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = state.isLabelMenuExpanded,
                onExpandedChange = { viewModel.onLabelMenuExpanded(it) },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.recordLabel,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(text = "Record Label", color = Color(0xFF6B7280))
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isLabelMenuExpanded)
                    },
                    modifier = Modifier
                        .testTag(UiTestTags.CREATE_ALBUM_LABEL_FIELD)
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = createAlbumFieldColors()
                )

                DropdownMenu(
                    expanded = state.isLabelMenuExpanded,
                    onDismissRequest = { viewModel.onLabelMenuExpanded(false) }
                ) {
                    CreateAlbumUiState.recordLabels.forEach { labelOption ->
                        DropdownMenuItem(
                            text = { Text(text = labelOption) },
                            onClick = { viewModel.onRecordLabelSelected(labelOption) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            CreateAlbumTextField(
                value = state.description,
                onValueChange = viewModel::onDescriptionChange,
                placeholder = "Description",
                minLines = 4,
                maxLines = 4,
                modifier = Modifier.testTag(UiTestTags.CREATE_ALBUM_DESCRIPTION_FIELD)
            )

            state.errorMessage?.let { msg ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = msg,
                    color = Color(0xFFB3261E),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.submit(onCreateSuccess) },
                enabled = !state.isSubmitting,
                modifier = Modifier
                    .testTag(UiTestTags.CREATE_ALBUM_SUBMIT_BUTTON)
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B35BD))
            ) {
                Text(
                    text = if (state.isSubmitting) "Creando…" else "Create Album",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(
                onClick = onSaveDraft,
                modifier = Modifier
                    .testTag(UiTestTags.CREATE_ALBUM_SAVE_DRAFT_BUTTON)
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
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        placeholder = {
            Text(text = placeholder, color = Color(0xFF6B7280))
        },
        trailingIcon = trailingIcon,
        modifier = modifier.fillMaxWidth(),
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

private object PreviewCreateAlbumRepository : AlbumRepository {
    override suspend fun getAlbums(): List<Album> = emptyList()
    override suspend fun getAlbumById(id: Int): Album? = null
    override suspend fun createAlbum(newAlbum: NewAlbum): Album = Album(
        id = 1,
        name = newAlbum.name,
        cover = newAlbum.cover,
        releaseDate = newAlbum.releaseDateIso,
        description = newAlbum.description,
        genre = newAlbum.genre,
        recordLabel = newAlbum.recordLabel
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateAlbumScreenPreview() {
    CreateAlbumScreen(
        viewModel = CreateAlbumViewModel(CreateAlbumUseCase(PreviewCreateAlbumRepository)),
        onBack = {},
        onBottomNavSelected = {}
    )
}
