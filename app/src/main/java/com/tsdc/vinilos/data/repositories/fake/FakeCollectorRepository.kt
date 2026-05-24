package com.tsdc.vinilos.data.repositories.fake

import com.tsdc.vinilos.domain.models.Collector
import com.tsdc.vinilos.domain.repositories.CollectorRepository

class FakeCollectorRepository : CollectorRepository {

    private val collectors = listOf(
        Collector(
            id = 1,
            name = "Collector Demo",
            telephone = "+57 300 000 0001",
            email = "collector1@example.com",
            albumCount = 2
        ),
        Collector(
            id = 2,
            name = "Collector QA",
            telephone = "+57 300 000 0002",
            email = "collector2@example.com",
            albumCount = 1
        )
    )

    override suspend fun getCollectors(): List<Collector> = collectors

    override suspend fun getCollectorById(id: Int): Collector =
        collectors.firstOrNull { it.id == id }
            ?: throw NoSuchElementException("Collector with id $id was not found")
}