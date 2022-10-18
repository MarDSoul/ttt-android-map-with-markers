package app.mardsoul.mapmarkers.ui

import android.location.Geocoder
import androidx.lifecycle.*
import app.mardsoul.mapmarkers.domain.MarkerUseCase
import app.mardsoul.mapmarkers.domain.Place
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel(
    private val markerUseCase: MarkerUseCase
) : ViewModel() {

    val markerLiveData = markerUseCase.getPlaces().asLiveData()

    private val _searchPlaceResult = MutableLiveData<LatLng>()
    val searchPlaceResult: LiveData<LatLng> = _searchPlaceResult

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

    fun searchPlace(locationName: String, geocoder: Geocoder) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val listAddress = geocoder.getFromLocationName(locationName, 1)
                if (listAddress.isNotEmpty()) {
                    _searchPlaceResult.postValue(
                        LatLng(listAddress[0].latitude, listAddress[0].longitude)
                    )
                }
            }
        }
    }
}

class SharedViewModelFactory(private val markerUseCase: MarkerUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedViewModel(markerUseCase) as T
    }
}