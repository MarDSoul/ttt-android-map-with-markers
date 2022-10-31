package app.mardsoul.mapmarkers.ui.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import app.mardsoul.mapmarkers.R
import app.mardsoul.mapmarkers.databinding.LayoutMarkerInfoBinding
import app.mardsoul.mapmarkers.domain.Place
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    private val binding: LayoutMarkerInfoBinding by lazy {
        val inflater = LayoutInflater.from(context)
        LayoutMarkerInfoBinding.inflate(inflater, null, false)
    }

    override fun getInfoContents(marker: Marker): View? {
        val place = marker.tag as Place
        with(binding) {
            nameTextView.text = place.name ?: context.getString(R.string.item_def_name_text)
            annotationTextView.text =
                place.annotation ?: context.getString(R.string.item_def_annot_text)
        }
        return binding.root
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }
}