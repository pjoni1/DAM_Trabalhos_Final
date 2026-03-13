package com.example.fuelfinder.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fuelfinder.R
import com.example.fuelfinder.data.model.FuelStation
import com.example.fuelfinder.databinding.ItemStationBinding

class StationAdapter(
    private val onClick: (FuelStation) -> Unit
) : RecyclerView.Adapter<StationAdapter.StationViewHolder>() {

    private var stations: List<FuelStation> = emptyList()
    private var currentFuelType: FuelType = FuelType.GASOLINE

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<FuelStation>, fuelType: FuelType) {
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

        fun bind(station: FuelStation, fuelType: FuelType, onClick: (FuelStation) -> Unit) {
            binding.tvStationName.text = station.nome
            binding.tvStationAddress.text = "${station.marca}, ${station.localidade}"

            val targetType = if (fuelType == FuelType.GASOLINE) {
                "Gasolina 95"
            } else {
                "Gasóleo Simples"
            }

            val priceItem = station.combustiveis.find { it.tipoCombustivel.contains(targetType, ignoreCase = true) }
            val price = priceItem?.preco

            if (price != null) {
                binding.tvPrice.text = "€$price/L" // DGEG returns price as string, e.g., "1.729"
            } else {
                binding.tvPrice.text = binding.root.context.getString(R.string.unknown_price)
            }

            binding.root.setOnClickListener {
                onClick(station)
            }
        }
    }
}
