package com.tsdc.vinilos.data.remote.dto

import java.util.Date

data class ArtistDto(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: Date
)
