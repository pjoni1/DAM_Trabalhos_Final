package com.example.fuelfinder.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.fuelfinder.R
import com.example.fuelfinder.data.api.RetrofitClient
import com.example.fuelfinder.data.repository.FuelRepository
import com.example.fuelfinder.databinding.FragmentDetailsBinding
import com.example.fuelfinder.utils.Resource
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val stationId: String? by lazy {
        arguments?.getString("stationId")
    }

    private val viewModel: DetailsViewModel by viewModels {
        DetailsViewModelFactory(FuelRepository(RetrofitClient.fuelApi), stationId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stationState.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.contentGroup.visibility = View.GONE
                            binding.tvError.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.contentGroup.visibility = View.VISIBLE
                            binding.tvError.visibility = View.GONE

                            val station = resource.data
                            binding.tvStationName.text = station.name
                            binding.tvStationAddress.text = "${station.address}, ${station.city}"

                            val gasPriceStr = station.gasolinePrice?.let { getString(R.string.price_format, it) } ?: getString(R.string.unknown_price)
                            val dieselPriceStr = station.dieselPrice?.let { getString(R.string.price_format, it) } ?: getString(R.string.unknown_price)

                            binding.tvGasolinePrice.text = gasPriceStr
                            binding.tvDieselPrice.text = dieselPriceStr
                            binding.tvLastUpdated.text = getString(R.string.last_updated, station.lastUpdated)
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.contentGroup.visibility = View.GONE
                            binding.tvError.visibility = View.VISIBLE
                            binding.tvError.text = resource.message
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
