package com.tsdc.vinilos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsdc.vinilos.domain.models.NewAlbum
import com.tsdc.vinilos.domain.usecases.CreateAlbumUseCase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateAlbumUiState(
    val title: String = "",
    val releaseDate: String = "",
    val coverUrl: String = "",
    val genre: String = "",
    val recordLabel: String = "",
    val description: String = "",
    val isGenreMenuExpanded: Boolean = false,
    val isLabelMenuExpanded: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
) {
    companion object {
        val genres = listOf("Classical", "Salsa", "Rock", "Folk")
        val recordLabels = listOf(
            "Sony Music",
            "EMI",
            "Discos Fuentes",
            "Elektra",
            "Fania Records"
        )
    }
}

class CreateAlbumViewModel(
    private val createAlbumUseCase: CreateAlbumUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateAlbumUiState())
    val uiState: StateFlow<CreateAlbumUiState> = _uiState.asStateFlow()

    fun onTitleChange(value: String) = _uiState.update { it.copy(title = value, errorMessage = null) }

    fun onCoverUrlChange(value: String) = _uiState.update { it.copy(coverUrl = value, errorMessage = null) }

    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value, errorMessage = null) }

    fun onReleaseDateMillis(millis: Long) {
        val formatted = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(java.util.Date(millis))
        _uiState.update { it.copy(releaseDate = formatted, errorMessage = null) }
    }

    fun onGenreMenuExpanded(expanded: Boolean) =
        _uiState.update { it.copy(isGenreMenuExpanded = expanded) }

    fun onGenreSelected(value: String) =
        _uiState.update { it.copy(genre = value, isGenreMenuExpanded = false, errorMessage = null) }

    fun onLabelMenuExpanded(expanded: Boolean) =
        _uiState.update { it.copy(isLabelMenuExpanded = expanded) }

    fun onRecordLabelSelected(value: String) =
        _uiState.update { it.copy(recordLabel = value, isLabelMenuExpanded = false, errorMessage = null) }

    fun resetForm() {
        _uiState.value = CreateAlbumUiState()
    }

    fun submit(onSuccess: () -> Unit) {
        val s = _uiState.value
        val validationError = validate(s)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        val releaseIso = try {
            usDateToIsoNoonUtc(s.releaseDate)
        } catch (e: ParseException) {
            _uiState.update { it.copy(errorMessage = "Fecha inválida. Usa el calendario (mm/dd/aaaa).") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            try {
                createAlbumUseCase(
                    NewAlbum(
                        name = s.title.trim(),
                        cover = s.coverUrl.trim(),
                        releaseDateIso = releaseIso,
                        description = s.description.trim(),
                        genre = s.genre,
                        recordLabel = s.recordLabel
                    )
                )
                resetForm()
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = e.message ?: "No se pudo crear el álbum"
                    )
                }
            }
        }
    }

    private fun validate(s: CreateAlbumUiState): String? = when {
        s.title.isBlank() -> "Indica el título del álbum."
        s.releaseDate.isBlank() -> "Indica la fecha de lanzamiento."
        s.coverUrl.isBlank() -> "Indica la URL de la portada."
        s.genre.isBlank() -> "Selecciona un género."
        s.recordLabel.isBlank() -> "Selecciona un sello discográfico."
        s.description.isBlank() -> "Indica la descripción."
        else -> null
    }

    /**
     * Convierte mm/dd/aaaa a ISO-8601 con mediodía UTC (compatible con Joi en el backend).
     */
    private fun usDateToIsoNoonUtc(usDate: String): String {
        val inFmt = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        inFmt.isLenient = false
        val date = inFmt.parse(usDate.trim()) ?: throw ParseException("bad date", 0)
        val outFmt = SimpleDateFormat("yyyy-MM-dd'T'12:00:00.000'Z'", Locale.US)
        outFmt.timeZone = TimeZone.getTimeZone("UTC")
        return outFmt.format(date)
    }
}
