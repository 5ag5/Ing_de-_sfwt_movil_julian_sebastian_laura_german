package com.tsdc.vinilos.domain.usecases

import com.tsdc.vinilos.domain.models.Collector
import com.tsdc.vinilos.domain.repositories.CollectorRepository

class GetCollectorsUseCase(private val repository: CollectorRepository) {
    suspend operator fun invoke(): List<Collector> {
        return repository.getCollectors()
    }
}
