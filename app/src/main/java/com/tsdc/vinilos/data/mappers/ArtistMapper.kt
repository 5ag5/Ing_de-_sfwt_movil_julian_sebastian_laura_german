package com.tsdc.vinilos.data.mappers

import com.tsdc.vinilos.data.local.entities.ArtistEntity
import com.tsdc.vinilos.data.remote.dto.ArtistDto
import com.tsdc.vinilos.domain.models.Artist

fun ArtistDto.toDomain(): Artist = Artist(
    id = id,
    name = name,
    image = image,
    description = description,
    birthDate = birthDate
)

fun Artist.toEntity(): ArtistEntity = ArtistEntity(
    id = id,
    name = name,
    image = image,
    description = description,
    birthDate = birthDate
)

fun ArtistEntity.toDomain(): Artist = Artist(
    id = id,
    name = name,
    image = image,
    description = description,
    birthDate = birthDate
)