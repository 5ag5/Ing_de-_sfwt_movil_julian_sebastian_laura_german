package com.tsdc.vinilos.data.remote.dto

data class CollectorDto(
    val id: Int,
    val name: String,
    val telephone: String,
    val email: String,
    val collectorAlbums: List<CollectorAlbumDto> = emptyList()
)

data class CollectorAlbumDto(
    val id: Int,
    val price: Double,
    val status: String
)
