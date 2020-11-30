package com.example.moviedbapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.moviedbapp.R
import com.example.moviedbapp.databinding.UpcomingMovieBinding
import com.example.moviedbapp.model.MovieResult


class MovieAdapter(private val listener : OnItemClickListener) : PagingDataAdapter<MovieResult, MovieAdapter.MovieViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = UpcomingMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class MovieViewHolder(private val binding: UpcomingMovieBinding) :
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

        fun bind(movie: MovieResult) {
            with(binding) {
                Glide.with(itemView)
                    .load("${movie.baseUrl}${movie.posterPath}")
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)
                title.text = movie.originalTitle
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(movie: MovieResult)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<MovieResult>() {
            override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean =
                oldItem == newItem
        }
    }

}