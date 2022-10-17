package app.mardsoul.mapmarkers.data

import app.mardsoul.mapmarkers.database.AppDatabaseDao
import app.mardsoul.mapmarkers.database.PlaceEntity
import app.mardsoul.mapmarkers.domain.MarkerUseCase
import app.mardsoul.mapmarkers.domain.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MarkerUseCaseImpl(
    private val databaseDao: AppDatabaseDao
) : MarkerUseCase {
    override suspend fun addPlace(place: Place) =
        withContext(Dispatchers.IO) {
            databaseDao.addPlace(convertPlaceToEntity(place))
        }


    override fun getPlaces(): Flow<List<Place>> {
        return databaseDao.getPlaces()
            .map { list -> list.map { it.toPlace() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun updatePlace(place: Place) =
        withContext(Dispatchers.IO) {
            databaseDao.updatePlace(convertPlaceToEntity(place))
        }

    override suspend fun deletePlace(place: Place) =
        withContext(Dispatchers.IO) {
            databaseDao.deletePlace(convertPlaceToEntity(place))
        }

    override suspend fun clearPlacesList() =
        withContext(Dispatchers.IO) {
            databaseDao.clearAll()
        }

    private fun convertPlaceToEntity(place: Place): PlaceEntity {
        return PlaceEntity(
            id = place.id,
            name = place.name,
            annotation = place.annotation,
            latitude = place.latLng.latitude,
            longitude = place.latLng.longitude
        )
    }
}