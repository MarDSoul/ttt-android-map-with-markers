package app.mardsoul.mapmarkers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import app.mardsoul.mapmarkers.domain.MarkerUseCase
import app.mardsoul.mapmarkers.domain.Place
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class SharedViewModel(
    private val markerUseCase: MarkerUseCase
) : ViewModel() {
    val markerLiveData = markerUseCase.getPlaces().asLiveData()

    fun addPlace(latLng: LatLng) {
        val newPlace = Place(latLng = latLng)
        viewModelScope.launch {
            markerUseCase.addPlace(newPlace)
        }
    }

    fun clearPlaces() {
        viewModelScope.launch {
            markerUseCase.clearPlacesList()
        }
    }
}

class SharedViewModelFactory(private val markerUseCase: MarkerUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedViewModel(markerUseCase) as T
    }
}