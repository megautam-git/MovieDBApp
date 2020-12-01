package com.example.moviedbapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.moviedbapp.R
import com.example.moviedbapp.databinding.NowplayingBinding
import com.example.moviedbapp.model.NowPlayingResult

class NowPlayingMovieAdapter(private val listener : OnItemClickListener) : PagingDataAdapter<NowPlayingResult, NowPlayingMovieAdapter.MovieViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = NowplayingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class MovieViewHolder(private val binding: NowplayingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION){
                    val item = getItem(position)
                    if (item!=null){
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(nowPlayingResult: NowPlayingResult) {
            with(binding) {
                Glide.with(itemView)
                    .load("${nowPlayingResult.baseUrl}${nowPlayingResult.posterPath}")
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)
                title.text = nowPlayingResult.originalTitle
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(nowPlayingResult: NowPlayingResult)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<NowPlayingResult>() {
            override fun areItemsTheSame(oldItem: NowPlayingResult, newItem: NowPlayingResult): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: NowPlayingResult, newItem: NowPlayingResult): Boolean =
                oldItem == newItem
        }
    }

}