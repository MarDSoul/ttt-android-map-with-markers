package app.mardsoul.mapmarkers.ui.map

import android.content.Context
import android.location.Geocoder

class GeocoderFactory(private val context: Context) {
    fun createGeocoder() = Geocoder(context)
}