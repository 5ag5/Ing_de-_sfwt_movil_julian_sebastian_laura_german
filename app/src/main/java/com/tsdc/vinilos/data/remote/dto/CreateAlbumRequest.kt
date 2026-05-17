package com.tsdc.vinilos.data.remote.dto

/**
 * Cuerpo POST /albums (sin id; el servidor lo asigna).
 */
data class CreateAlbumRequest(
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String
)
