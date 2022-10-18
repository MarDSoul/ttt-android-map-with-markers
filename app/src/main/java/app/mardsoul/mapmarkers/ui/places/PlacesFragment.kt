package app.mardsoul.mapmarkers.ui.places

import android.os.Bundle
import android.view.View
import app.mardsoul.mapmarkers.databinding.FragmentPlacesBinding
import app.mardsoul.mapmarkers.domain.Place
import app.mardsoul.mapmarkers.ui.BaseFragment

class PlacesFragment : BaseFragment<FragmentPlacesBinding>(FragmentPlacesBinding::inflate) {

    private val adapter = PlacesAdapter()


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
}