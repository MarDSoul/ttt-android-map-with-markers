package app.mardsoul.mapmarkers.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import app.mardsoul.mapmarkers.R
import app.mardsoul.mapmarkers.app
import app.mardsoul.mapmarkers.databinding.FragmentMapsBinding
import app.mardsoul.mapmarkers.domain.Place
import app.mardsoul.mapmarkers.ui.BaseFragment
import app.mardsoul.mapmarkers.ui.showSnackbar
import app.mardsoul.mapmarkers.ui.toLatLng
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad

class MapsFragment : BaseFragment<FragmentMapsBinding>(FragmentMapsBinding::inflate) {

    private var googleMap: GoogleMap? = null

    private val geocoderFactory: GeocoderFactory by lazy { requireContext().app.geocoderFactory }

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                updateLocationUi(googleMap)
            }
        }

    private fun checkPermission() {
        when {
            isGranted() -> {
                getDeviceLocation()
            }
            isRatio() -> {
                binding.root.showSnackbar(
                    msg = resources.getString(R.string.permission_requared),
                    length = Snackbar.LENGTH_INDEFINITE,
                    actionMessage = resources.getString(R.string.ok)
                ) { requestPermission() }
            }
            else -> {
                requestPermission()
            }
        }
    }

    private fun requestPermission() {
        locationPermissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun isGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isRatio(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val locationResult = fusedLocationProviderClient.lastLocation
        locationResult.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                moveCamera(task.result.toLatLng())
            }
        }
    }

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
                setInfoWindowAdapter(MarkerInfoWindowAdapter(requireContext()))
                uiSettings.isZoomControlsEnabled = true
                updateLocationUi(this)
            }
            viewModel.markerLiveData.observe(viewLifecycleOwner) { refreshMarkers(it) }
            viewModel.searchPlaceResult.observe(viewLifecycleOwner) { moveCamera(it) }
        }
        binding.searchButton.setOnClickListener { search(binding.searchEditText.text.toString()) }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUi(googleMap: GoogleMap?) {
        googleMap?.let { map ->
            if (isGranted()) {
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
                getDeviceLocation()
            } else {
                map.isMyLocationEnabled = false
                map.uiSettings.isMyLocationButtonEnabled = false
                checkPermission()
            }
        }
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
        val marker = googleMap?.addMarker {
            position(place.latLng)
            icon(BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_baseline_pin_24))
        }
        marker?.tag = place
    }

    private fun search(locationName: String) {
        if (locationName.isNotEmpty()) {
            viewModel.searchPlace(locationName, geocoderFactory.createGeocoder())
        }
    }
}