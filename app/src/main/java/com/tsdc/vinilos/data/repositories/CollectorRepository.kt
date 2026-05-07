package com.tsdc.vinilos.data.repositories

import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.domain.models.Collector
import com.tsdc.vinilos.domain.repositories.CollectorRepository as CollectorRepositoryInterface

class CollectorRepository(
    private val serviceAdapter: ServiceAdapter
) : CollectorRepositoryInterface {
    override suspend fun getCollectors(): List<Collector> = serviceAdapter.fetchCollectors()
}
