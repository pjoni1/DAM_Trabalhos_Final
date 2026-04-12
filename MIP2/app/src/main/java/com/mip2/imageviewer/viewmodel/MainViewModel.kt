package com.mip2.imageviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mip2.imageviewer.model.ImageItem
import com.mip2.imageviewer.repository.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Representação de classes seladas para cobrir eficazmente as oscilações de UI.
sealed class UiState {
    object Loading : UiState()
    data class Success(val images: List<ImageItem>) : UiState()
    data class Error(val message: String) : UiState()
}

class MainViewModel(private val repository: ImageRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    // Temporário: Key de testes a instanciar (Normalmente isto devia residir no buildConfig/local.properties).
    // Substituir pela chave de developers real da Unsplash se pretendido em runtime.
    private val API_KEY = "DUMMY_KEY_CHANGEME" 

    init {
        // Ao inicializar o MainViewModel, lança automaticamente o primeiro pedido de API
        fetchImages()
    }

    fun fetchImages() {
        // Corre um trabalho de fundo numa Thread associada ao Lifecycle do MainViewModel
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val images = repository.getImages(API_KEY)
                _uiState.value = UiState.Success(images)
            } catch (e: Exception) {
                // Previne falhas de parsing, timeout HTTP, conectividade etc
                _uiState.value = UiState.Error(e.message ?: "Ocorreu um erro desconhecido na rede.")
            }
        }
    }
}

// Uma Factory é estritamente necessária ao construirmos ViewModels com parâmetros específicos
class MainViewModelFactory(private val repository: ImageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Classe ViewModel fornecida para a Factory é desconhecida.")
    }
}
