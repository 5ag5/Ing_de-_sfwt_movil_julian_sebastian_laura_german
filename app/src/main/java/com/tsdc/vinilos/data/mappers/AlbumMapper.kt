package com.tsdc.vinilos.data.mappers

import com.tsdc.vinilos.data.local.entities.AlbumEntity
import com.tsdc.vinilos.data.remote.dto.AlbumDto
import com.tsdc.vinilos.domain.models.Album

fun AlbumDto.toDomain(): Album = Album(
    id = id,
    name = name,
    cover = cover,
    releaseDate = releaseDate,
    description = description,
    genre = genre,
    recordLabel = recordLabel
)

fun Album.toEntity(): AlbumEntity = AlbumEntity(
    id = id,
    name = name,
    cover = cover,
    releaseDate = releaseDate,
    description = description,
    genre = genre,
    recordLabel = recordLabel
)

fun AlbumEntity.toDomain(): Album = Album(
    id = id,
    name = name,
    cover = cover,
    releaseDate = releaseDate,
    description = description,
    genre = genre,
    recordLabel = recordLabel
)
