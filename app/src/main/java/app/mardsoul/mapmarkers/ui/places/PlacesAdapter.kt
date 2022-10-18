package app.mardsoul.mapmarkers.ui.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.mardsoul.mapmarkers.R
import app.mardsoul.mapmarkers.databinding.ItemPlaceBinding
import app.mardsoul.mapmarkers.domain.Place

class PlacesAdapter : RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder>() {

    private var placesList = mutableListOf<Place>()

    fun setPlacesList(data: List<Place>) {
        placesList = data.toMutableList()
        notifyDataSetChanged()
    }

    inner class PlacesViewHolder(
        private val binding: ItemPlaceBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val itemViewContext = itemView.context
        fun bind(place: Place) {
            with(binding) {
                nameTextView.text =
                    place.name ?: itemViewContext.getString(R.string.item_def_name_text)
                annotationTextView.text =
                    place.annotation ?: itemViewContext.getString(R.string.item_def_annot_text)
                latTextView.text =
                    itemViewContext.getString(R.string.item_lat_text, place.latLng.latitude)
                lonTextView.text =
                    itemViewContext.getString(R.string.item_lon_text, place.latLng.longitude)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlaceBinding.inflate(inflater, parent, false)
        return PlacesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        holder.bind(placesList[position])
    }

    override fun getItemCount(): Int = placesList.size
}