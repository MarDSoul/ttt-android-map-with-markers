package app.mardsoul.mapmarkers.ui

import android.content.Context
import android.location.Location
import android.view.View
import androidx.appcompat.widget.PopupMenu
import app.mardsoul.mapmarkers.R
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

fun View.showPopupMenu(context: Context, editAction: () -> Unit, deleteAction: () -> Unit) {
    val popupMenu = PopupMenu(context, this).apply {
        inflate(R.menu.menu_marker)
        setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_menu_item -> {
                    editAction()
                    true
                }
                R.id.delete_menu_item -> {
                    deleteAction()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
    popupMenu.show()
}

fun Location.toLatLng() = LatLng(latitude, longitude)