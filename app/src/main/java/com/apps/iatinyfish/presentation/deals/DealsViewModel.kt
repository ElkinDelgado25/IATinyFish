package com.apps.iatinyfish.presentation.deals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.iatinyfish.data.model.ProductOffer
import com.apps.iatinyfish.data.repository.DealsRepository
import com.apps.iatinyfish.data.repository.DemoDealsRepository
import com.apps.iatinyfish.data.repository.RemoteDealsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DealsUiState(
    val query: String = "Nintendo Switch OLED",
    val isLoading: Boolean = false,
    val offers: List<ProductOffer> = emptyList(),
    val selectedOffer: ProductOffer? = null,
    val error: String? = null,
)

class DealsViewModel(
    private val repository: DealsRepository = RemoteDealsRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(DealsUiState())
    val uiState: StateFlow<DealsUiState> = _uiState.asStateFlow()

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query, error = null)
    }

    fun search() {
        val query = _uiState.value.query.trim()
        if (query.isEmpty()) {
            _uiState.value = _uiState.value.copy(error = "Escribe un producto para buscar.")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            runCatching { repository.search(query) }
                .onSuccess { offers -> _uiState.value = _uiState.value.copy(isLoading = false, offers = offers) }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "No se pudo completar la búsqueda.",
                    )
                }
        }
    }

    fun selectOffer(offer: ProductOffer?) {
        _uiState.value = _uiState.value.copy(selectedOffer = offer)
    }
}
