package app.mardsoul.mapmarkers.ui

import android.location.Location
import android.view.View
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    msg: String,
    length: Int,
    actionMessage: String?,
    action: (View) -> Unit
) {
    val snackbar = Snackbar.make(this, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) { action(this) }.show()
    } else {
        snackbar.show()
    }
}

fun Location.toLatLng() = LatLng(latitude, longitude)