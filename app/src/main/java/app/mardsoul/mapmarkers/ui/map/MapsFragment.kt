package app.mardsoul.mapmarkers.ui.map

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import app.mardsoul.mapmarkers.R
import app.mardsoul.mapmarkers.app
import app.mardsoul.mapmarkers.databinding.FragmentMapsBinding
import app.mardsoul.mapmarkers.domain.Place
import app.mardsoul.mapmarkers.ui.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad

class MapsFragment : BaseFragment<FragmentMapsBinding>(FragmentMapsBinding::inflate) {

    private var googleMap: GoogleMap? = null

    private val geocoderFactory: GeocoderFactory by lazy { requireContext().app.geocoderFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        lifecycleScope.launchWhenCreated {
            googleMap = mapFragment.awaitMap().apply {
                awaitMapLoad()
                setOnMapLongClickListener {
                    viewModel.addPlace(it)
                    showAddToast(it)
                }
            }
            viewModel.markerLiveData.observe(viewLifecycleOwner) { refreshMarkers(it) }
            viewModel.searchPlaceResult.observe(viewLifecycleOwner) { moveCamera(it) }
        }
        binding.searchButton.setOnClickListener { search(binding.searchEditText.text.toString()) }
    }

    private fun moveCamera(latLng: LatLng) {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
    }

    private fun showAddToast(latLng: LatLng) {
        val toastText =
            getString(R.string.add_place_toast_text, latLng.latitude, latLng.longitude)
        Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()
    }

    private fun refreshMarkers(placeList: List<Place>) {
        googleMap?.clear()
        placeList.forEach { addPlace(it) }
    }

    private fun addPlace(place: Place) {
        googleMap?.addMarker {
            title(place.name)
            position(place.latLng)
            icon(BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_baseline_pin_24))
        }
    }

    private fun search(locationName: String) {
        if (locationName.isNotEmpty()) {
            viewModel.searchPlace(locationName, geocoderFactory.createGeocoder())
        }
    }
}