package com.tsdc.vinilos.domain.usecases

import com.tsdc.vinilos.domain.models.Collector
import com.tsdc.vinilos.domain.repositories.CollectorRepository

class GetCollectorByIdUseCase(private val repository: CollectorRepository) {
    suspend operator fun invoke(id: Int): Collector = repository.getCollectorById(id)
}
