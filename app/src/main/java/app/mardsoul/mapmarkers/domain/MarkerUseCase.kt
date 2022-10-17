package app.mardsoul.mapmarkers.domain

import kotlinx.coroutines.flow.Flow

interface MarkerUseCase {
    suspend fun addPlace(place: Place)
    fun getPlaces(): Flow<List<Place>>
    suspend fun updatePlace(place: Place)
    suspend fun deletePlace(place: Place)
    suspend fun clearPlacesList()
}