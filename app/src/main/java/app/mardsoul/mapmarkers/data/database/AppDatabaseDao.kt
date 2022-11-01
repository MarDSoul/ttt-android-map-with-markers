package app.mardsoul.mapmarkers.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDatabaseDao {

    @Insert
    suspend fun addPlace(placeEntity: PlaceEntity)

    @Query("SELECT * FROM place")
    fun getPlaces(): Flow<List<PlaceEntity>>

    @Update
    suspend fun updatePlace(placeEntity: PlaceEntity)

    @Delete
    suspend fun deletePlace(placeEntity: PlaceEntity)

    @Query("DELETE FROM place")
    suspend fun clearAll()
}