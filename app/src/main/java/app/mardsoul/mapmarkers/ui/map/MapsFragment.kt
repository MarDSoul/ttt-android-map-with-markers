package app.mardsoul.mapmarkers.ui.map

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import app.mardsoul.mapmarkers.R
import app.mardsoul.mapmarkers.databinding.FragmentMapsBinding
import app.mardsoul.mapmarkers.domain.Place
import app.mardsoul.mapmarkers.ui.BaseFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad

class MapsFragment : BaseFragment<FragmentMapsBinding>(FragmentMapsBinding::inflate) {

    private var googleMap: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        lifecycleScope.launchWhenCreated {
            googleMap = mapFragment.awaitMap().apply { awaitMapLoad() }
        }
        viewModel.markerLiveData.observe(viewLifecycleOwner) { refreshMarkers(it) }
    }

    private fun refreshMarkers(placeList: List<Place>) {
        placeList.forEach { addPlace(it) }
        Toast.makeText(requireContext(), "Received new places list", Toast.LENGTH_SHORT).show()
    }

    private fun addPlace(place: Place) {
        googleMap?.addMarker {
            title(place.name)
            position(place.latLng)
            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_pin_24))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MapsFragment()
    }
}