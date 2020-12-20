package com.gs.moviedbapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.gs.moviedbapp.R
import com.gs.moviedbapp.model.FavoriteMovie
import com.gs.moviedbapp.util.RecyclerViewClickListener
import kotlinx.android.synthetic.main.item_movie.view.*

class FavoriteAdapter(val list:List<FavoriteMovie>, private val listener:RecyclerViewClickListener) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {


    interface OnItemClickCallback {
        fun onItemClick(favoriteMovie: FavoriteMovie)
    }

   inner class FavoriteViewHolder(itemview: View) :RecyclerView.ViewHolder(itemview) {
       fun bind(favoriteMovie: FavoriteMovie) {

           Glide.with(itemView.context)
                   .load("${favoriteMovie.baseUrl}${favoriteMovie.posterPath}")
                   .centerCrop()
                   .transition(DrawableTransitionOptions.withCrossFade())
                   .error(R.drawable.ic_error)
                   .into(itemView.imageView)
           itemView.title.text = favoriteMovie.title

       }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemview=LayoutInflater.from(parent.context).inflate(R.layout.item_movie,parent,false)
        return FavoriteViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
        listener.onRecyclerViewItemClick(it,list[position])
    }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}