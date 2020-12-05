package com.example.moviedbapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.moviedbapp.R
import com.example.moviedbapp.databinding.UpcomingBinding
import com.example.moviedbapp.model.UpcomingMovieResult


class UpcomingMovieAdapter(private val listener : OnItemClickListener) : PagingDataAdapter<UpcomingMovieResult, UpcomingMovieAdapter.MovieViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = UpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class MovieViewHolder(private val binding: UpcomingBinding) :
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

        fun bind(upcomingMovie: UpcomingMovieResult) {
            with(binding) {
                Glide.with(itemView)
                    .load("${upcomingMovie.baseUrl}${upcomingMovie.posterPath}")
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)
                title.text = upcomingMovie.originalTitle
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(upcomingMovie: UpcomingMovieResult)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<UpcomingMovieResult>() {
            override fun areItemsTheSame(oldItem: UpcomingMovieResult, newItem: UpcomingMovieResult): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: UpcomingMovieResult, newItem: UpcomingMovieResult): Boolean =
                oldItem == newItem
        }
    }

}