package com.mip2.imageviewer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mip2.imageviewer.databinding.ItemImageBinding
import com.mip2.imageviewer.model.ImageItem

/**
 * Adapter que liga o Data Model (ImageItem) às vistas inflacionadas da RecyclerView.
 */
class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private val images = mutableListOf<ImageItem>()

    // Utilizado para atualizar inteiramente a lista a partir do ViewModel.
    fun setImages(newImages: List<ImageItem>) {
        images.clear()
        images.addAll(newImages)
        notifyDataSetChanged() // Num processo avançado, poderiamos usar o DiffUtil para maior performance.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageItem) {
            // Glide encarregar-se-á do cache, descarregamento rápido assíncrono e formatação visual na IU.
            Glide.with(binding.root.context)
                .load(item.url)
                .centerCrop()
                .into(binding.imageViewThumbnail)

            binding.textViewTitle.text = item.title ?: "Sem descrição disponível"
        }
    }
}
