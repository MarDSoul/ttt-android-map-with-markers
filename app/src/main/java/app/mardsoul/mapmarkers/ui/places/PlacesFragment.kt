package app.mardsoul.mapmarkers.ui.places

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AlertDialog
import app.mardsoul.mapmarkers.R
import app.mardsoul.mapmarkers.databinding.DialogEditPlaceBinding
import app.mardsoul.mapmarkers.databinding.FragmentPlacesBinding
import app.mardsoul.mapmarkers.domain.Place
import app.mardsoul.mapmarkers.ui.BaseFragment
import app.mardsoul.mapmarkers.ui.showPopupMenu

class PlacesFragment : BaseFragment<FragmentPlacesBinding>(FragmentPlacesBinding::inflate),
    OnClickListener {

    private val adapter = PlacesAdapter(this as OnClickListener)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.markerLiveData.observe(viewLifecycleOwner) { renderData(it) }
    }

    private fun initViews() {
        binding.placesRecyclerView.adapter = adapter
        binding.clearButton.setOnClickListener {
            viewModel.clearPlaces()
            navigator.goBack()
        }
    }

    private fun renderData(placeList: List<Place>) {
        adapter.setPlacesList(placeList)
    }

    override fun onClick(view: View) {
        val place = view.tag as Place
        view.showPopupMenu(
            requireContext(),
            { showEditAlertDialog(place) },
            { viewModel.deletePlace(place) }
        )
    }

    fun showEditAlertDialog(place: Place) {
        val dialogBinding = DialogEditPlaceBinding.inflate(layoutInflater)
        with(dialogBinding) {
            nameEditText.setText(place.name)
            annotationEditText.setText(place.annotation)
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .setPositiveButton(R.string.positive_button_dialog) { _, _ ->
                val newPlace = place.copy(
                    name = dialogBinding.nameEditText.text.toString(),
                    annotation = dialogBinding.annotationEditText.text.toString()
                )
                viewModel.updatePlace(newPlace)
            }
            .create()
        dialog.show()
    }
}