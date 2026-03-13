package com.example.fuelfinder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.fuelfinder.R
import com.example.fuelfinder.data.api.RetrofitClient
import com.example.fuelfinder.data.model.FuelStation
import com.example.fuelfinder.data.repository.FuelRepository
import com.example.fuelfinder.databinding.FragmentMainBinding
import com.example.fuelfinder.utils.Resource
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(FuelRepository(RetrofitClient.fuelApi))
    }

    private lateinit var stationAdapter: StationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCitySelector()
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupCitySelector() {
        val cities = listOf("Lisboa", "Porto", "Coimbra", "Braga", "Faro")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        binding.autoCompleteCity.setAdapter(adapter)

        binding.autoCompleteCity.setOnItemClickListener { parent, _, position, _ ->
            val selectedCity = parent.getItemAtPosition(position) as String
            viewModel.fetchStations(selectedCity)
        }
    }

    private fun setupRecyclerView() {
        stationAdapter = StationAdapter { station ->
            val bundle = Bundle().apply {
                putString("stationId", station.id)
            }
            findNavController().navigate(R.id.action_mainFragment_to_detailsFragment, bundle)
        }
        binding.rvStations.adapter = stationAdapter
    }

    private fun setupListeners() {
        binding.toggleGroupFuelType.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                val fuelType = if (checkedId == R.id.btnGasoline) FuelType.GASOLINE else FuelType.DIESEL
                viewModel.setFuelType(fuelType)
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stations.collect { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                                binding.tvError.visibility = View.GONE
                                binding.rvStations.visibility = View.GONE
                            }
                            is Resource.Success -> {
                                binding.progressBar.visibility = View.GONE
                                binding.tvError.visibility = View.GONE
                                binding.rvStations.visibility = View.VISIBLE
                                // Update adapter data when stations arrive
                                stationAdapter.submitList(resource.data, viewModel.selectedFuelType.value)
                            }
                            is Resource.Error -> {
                                binding.progressBar.visibility = View.GONE
                                binding.tvError.visibility = View.VISIBLE
                                binding.rvStations.visibility = View.GONE
                                binding.tvError.text = resource.message
                            }
                        }
                    }
                }
                
                launch {
                    viewModel.selectedFuelType.collect { fuelType ->
                        // Only update view state if data is already loaded
                        if (viewModel.stations.value is Resource.Success) {
                            val stations = (viewModel.stations.value as Resource.Success).data
                            stationAdapter.submitList(stations, fuelType)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
