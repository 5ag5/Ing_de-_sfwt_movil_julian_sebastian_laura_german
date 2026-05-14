package com.tsdc.vinilos.data.repositories

import com.tsdc.vinilos.data.local.dao.CollectorDao
import com.tsdc.vinilos.data.mappers.toDomain
import com.tsdc.vinilos.data.mappers.toEntity
import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.domain.models.Collector
import com.tsdc.vinilos.domain.repositories.CollectorRepository as CollectorRepositoryInterface

class CollectorRepository(
    private val serviceAdapter: ServiceAdapter,
    private val collectorDao: CollectorDao
) : CollectorRepositoryInterface {

    override suspend fun getCollectors(): List<Collector> =
        try {
            val remote = serviceAdapter.fetchCollectors()
            collectorDao.replaceCollectors(remote.map { it.toEntity() })
            remote
        } catch (e: Exception) {
            val cached = collectorDao.getAllCollectors().map { it.toDomain() }
            if (cached.isEmpty()) throw e
            cached
        }

    override suspend fun getCollectorById(id: Int): Collector =
        try {
            val remote = serviceAdapter.fetchCollectorById(id)
            collectorDao.insert(remote.toEntity())
            remote
        } catch (e: Exception) {
            collectorDao.getCollectorById(id)?.toDomain() ?: throw e
        }
}
