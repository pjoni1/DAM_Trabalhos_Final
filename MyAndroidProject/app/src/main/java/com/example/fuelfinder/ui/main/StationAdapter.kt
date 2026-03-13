package com.example.fuelfinder.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fuelfinder.R
import com.example.fuelfinder.data.model.Station
import com.example.fuelfinder.databinding.ItemStationBinding

class StationAdapter(
    private val onClick: (Station) -> Unit
) : RecyclerView.Adapter<StationAdapter.StationViewHolder>() {

    private var stations: List<Station> = emptyList()
    private var currentFuelType: FuelType = FuelType.GASOLINE

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Station>, fuelType: FuelType) {
        stations = list
        currentFuelType = fuelType
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val binding = ItemStationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.bind(stations[position], currentFuelType, onClick)
    }

    override fun getItemCount(): Int = stations.size

    class StationViewHolder(private val binding: ItemStationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(station: Station, fuelType: FuelType, onClick: (Station) -> Unit) {
            binding.tvStationName.text = station.name
            binding.tvStationAddress.text = "${station.address}, ${station.city}"

            val price = if (fuelType == FuelType.GASOLINE) {
                station.gasolinePrice
            } else {
                station.dieselPrice
            }

            if (price != null) {
                binding.tvPrice.text = binding.root.context.getString(R.string.price_format, price)
            } else {
                binding.tvPrice.text = binding.root.context.getString(R.string.unknown_price)
            }

            binding.root.setOnClickListener {
                onClick(station)
            }
        }
    }
}
