package app.mardsoul.mapmarkers.domain

import com.google.android.gms.maps.model.LatLng

data class Place(
    val id: Int = 0,
    val name: String? = null,
    val annotation: String? = null,
    val latLng: LatLng
)