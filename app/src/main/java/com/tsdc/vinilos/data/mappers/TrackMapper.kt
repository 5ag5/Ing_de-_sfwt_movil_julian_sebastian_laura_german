package com.tsdc.vinilos.data.mappers

import com.tsdc.vinilos.data.remote.dto.TrackDto
import com.tsdc.vinilos.domain.models.Track

fun TrackDto.toDomain(): Track = Track(
    id = id,
    name = name,
    duration = duration
)
