package app.mardsoul.mapmarkers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import app.mardsoul.mapmarkers.domain.MarkerUseCase

class SharedViewModel(
    markerUseCase: MarkerUseCase
) : ViewModel() {
    val markerLiveData = markerUseCase.getPlaces().asLiveData()
}

class SharedViewModelFactory(private val markerUseCase: MarkerUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedViewModel(markerUseCase) as T
    }
}