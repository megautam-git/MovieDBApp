package com.gs.moviedbapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.gs.moviedbapp.R
import com.gs.moviedbapp.databinding.PopularBinding
import com.gs.moviedbapp.model.PopularResult

class PopularMovieAdapter(private val listener : OnItemClickListener) : PagingDataAdapter<PopularResult, PopularMovieAdapter.MovieViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = PopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class MovieViewHolder(private val binding: PopularBinding) :
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

        fun bind(popularmovieresult: PopularResult) {
            with(binding) {
                Glide.with(itemView)
                    .load("${popularmovieresult.baseUrl}${popularmovieresult.posterPath}")
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)
                title.text = popularmovieresult.originalTitle
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(popularmovieresult: PopularResult)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<PopularResult>() {
            override fun areItemsTheSame(oldItem: PopularResult, newItem: PopularResult): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PopularResult, newItem: PopularResult): Boolean =
                oldItem == newItem
        }
    }

}