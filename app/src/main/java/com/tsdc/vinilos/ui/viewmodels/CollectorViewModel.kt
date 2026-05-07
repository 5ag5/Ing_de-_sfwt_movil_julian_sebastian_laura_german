package com.tsdc.vinilos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsdc.vinilos.domain.models.Collector
import com.tsdc.vinilos.domain.usecases.GetCollectorsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CollectorViewModel(private val getCollectorsUseCase: GetCollectorsUseCase) : ViewModel() {

    private val _collectors = MutableStateFlow<List<Collector>>(emptyList())
    val collectors: StateFlow<List<Collector>> = _collectors

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCollectors() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _collectors.value = getCollectorsUseCase()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido al cargar coleccionistas"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
