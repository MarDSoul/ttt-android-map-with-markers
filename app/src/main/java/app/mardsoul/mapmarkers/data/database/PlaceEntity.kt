package app.mardsoul.mapmarkers.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.mardsoul.mapmarkers.domain.Place
import com.google.android.gms.maps.model.LatLng

@Entity(
    tableName = "place"
)
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String?,
    val annotation: String?,
    val latitude: Double,
    val longitude: Double
) {
    fun toPlace(): Place {
        return Place(
            id = id,
            name = name,
            annotation = annotation,
            latLng = LatLng(latitude, longitude)
        )
    }
}
