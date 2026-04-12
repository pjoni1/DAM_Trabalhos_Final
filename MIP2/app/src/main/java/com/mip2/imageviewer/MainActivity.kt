package com.mip2.imageviewer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mip2.imageviewer.api.ApiClient
import com.mip2.imageviewer.databinding.ActivityMainBinding
import com.mip2.imageviewer.repository.ImageRepository
import com.mip2.imageviewer.ui.ImageAdapter
import com.mip2.imageviewer.viewmodel.MainViewModel
import com.mip2.imageviewer.viewmodel.MainViewModelFactory
import com.mip2.imageviewer.viewmodel.UiState
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ImageAdapter

    // Inicialização controlada (Lazy) do gestor Model/Repository (Injeção de Dependência Manual)
    private val viewModel: MainViewModel by viewModels {
        val apiService = ApiClient.retrofitService
        val repository = ImageRepository(apiService)
        MainViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflaciona e conecta instantaneamente todo o sistema Views/XML de activity_main sem findViewByID
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = ImageAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchImages()
        }
    }

    private fun observeViewModel() {
        // Começa a observar (Subscribe) aos dados do Backend
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // Previne o "saltitar" UI caso o SwipeRefresh seja o provocador do load original
                        if (!binding.swipeRefreshLayout.isRefreshing) {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefreshLayout.isRefreshing = false
                        // Transmite as estruturas à vista que invoca o Glide
                        adapter.setImages(state.images)
                    }
                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefreshLayout.isRefreshing = false
                        Toast.makeText(this@MainActivity, "Erro: ${state.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
