package com.tsdc.vinilos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsdc.vinilos.domain.models.Collector
import com.tsdc.vinilos.domain.usecases.GetCollectorByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CollectorDetailViewModel(
    private val getCollectorByIdUseCase: GetCollectorByIdUseCase
) : ViewModel() {

    private val _collector = MutableStateFlow<Collector?>(null)
    val collector: StateFlow<Collector?> = _collector

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCollector(collectorId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = getCollectorByIdUseCase(collectorId)
                if (result == null) {
                    _error.value = "No se encontró el coleccionista"
                } else {
                    _collector.value = result
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido al cargar el coleccionista"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
