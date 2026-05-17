package com.tsdc.vinilos.domain.models

/**
 * Datos para crear un álbum (HU-07). [releaseDateIso] debe ser ISO-8601 aceptado por el backend.
 */
data class NewAlbum(
    val name: String,
    val cover: String,
    val releaseDateIso: String,
    val description: String,
    val genre: String,
    val recordLabel: String
)
