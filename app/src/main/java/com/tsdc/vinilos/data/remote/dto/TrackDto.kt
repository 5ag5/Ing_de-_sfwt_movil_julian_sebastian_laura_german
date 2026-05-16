package com.tsdc.vinilos.data.remote.dto

data class TrackDto(
    val id: Int,
    val name: String,
    val duration: String
)

data class TrackRequest(
    val name: String,
    val duration: String
)
