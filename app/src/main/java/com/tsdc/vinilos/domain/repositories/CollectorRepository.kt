package com.tsdc.vinilos.domain.repositories

import com.tsdc.vinilos.domain.models.Collector

interface CollectorRepository {
    suspend fun getCollectors(): List<Collector>
    suspend fun getCollectorById(id: Int): Collector
}
