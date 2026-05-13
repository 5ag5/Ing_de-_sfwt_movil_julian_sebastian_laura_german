package com.tsdc.vinilos

import com.tsdc.vinilos.domain.models.Collector
import com.tsdc.vinilos.domain.repositories.CollectorRepository

private val sampleCollectors = listOf(
    Collector(
        id = 100,
        name = "Manolo Bellon",
        telephone = "3502457896",
        email = "manollo@caracol.com.co"
    ),
    Collector(
        id = 101,
        name = "Jaime Monsalve",
        telephone = "3012357936",
        email = "jmonsalve@rtvc.com.co"
    )
)

class FakeCollectorRepository(
    private val collectors: List<Collector> = sampleCollectors
) : CollectorRepository {

    override suspend fun getCollectors(): List<Collector> = collectors

    override suspend fun getCollectorById(id: Int): Collector =
        collectors.first { it.id == id }
}
