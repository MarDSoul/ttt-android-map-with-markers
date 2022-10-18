package app.mardsoul.mapmarkers.ui.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import app.mardsoul.mapmarkers.R
import app.mardsoul.mapmarkers.app
import app.mardsoul.mapmarkers.databinding.FragmentMapsBinding
import app.mardsoul.mapmarkers.domain.Place
import app.mardsoul.mapmarkers.ui.BaseFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
        }
        binding.searchButton.setOnClickListener { search(binding.searchEditText.text.toString()) }
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
            icon(bitmapDescriptorFromVector(R.drawable.ic_baseline_pin_24))
        }
    }

    /**
     * Taken from ApiDemos on GitHub:
     * https://github.com/googlemaps/android-samples/blob/master/ApiDemos/kotlin/app/src/main/java/com/example/kotlindemos/MarkerDemoActivity.kt
     */
    private fun bitmapDescriptorFromVector(@DrawableRes vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, vectorResId, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun search(locationName: String) {
        if (locationName.isNotEmpty()) {
            viewModel.searchPlace(locationName, geocoderFactory.createGeocoder())
        }
    }
}