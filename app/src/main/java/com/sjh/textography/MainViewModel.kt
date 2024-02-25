package com.sjh.textography

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val steganographyManager: SteganographyManager
) : ViewModel() {

    data class MainUiState(
        val embeddedBitmap: Bitmap? = null,
        val isWorking: Boolean = false
    )

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            steganographyManager.progressFlow.collect { isWorking ->
                _uiState.update { state ->
                    state.copy(isWorking = isWorking)
                }
            }
        }
    }

    fun embedTextIntoBitmap(bitmap: Bitmap, text: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val embeddedBitmap = steganographyManager.embedTextIntoBitmap(bitmap, text)
                _uiState.update { state ->
                    state.copy(embeddedBitmap = embeddedBitmap)
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(SteganographyManager()) as T
            }
        }
    }
}