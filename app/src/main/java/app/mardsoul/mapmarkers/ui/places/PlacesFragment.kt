package app.mardsoul.mapmarkers.ui.places

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
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

    override fun onClick(view: View?) {
        view?.showPopupMenu(
            requireContext(),
            { viewModel.clearPlaces() }, //TODO ("add edit function")
            { viewModel.clearPlaces() } //TODO ("add delete function")
        )
    }
}