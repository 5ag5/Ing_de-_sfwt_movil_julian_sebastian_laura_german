package com.tsdc.vinilos.data.mappers

import com.tsdc.vinilos.data.local.entities.CollectorEntity
import com.tsdc.vinilos.data.remote.dto.CollectorDto
import com.tsdc.vinilos.domain.models.Collector

fun CollectorDto.toDomain(): Collector = Collector(
    id = id,
    name = name,
    telephone = telephone,
    email = email
)

fun Collector.toEntity(): CollectorEntity = CollectorEntity(
    id = id,
    name = name,
    telephone = telephone,
    email = email
)

fun CollectorEntity.toDomain(): Collector = Collector(
    id = id,
    name = name,
    telephone = telephone,
    email = email
)
