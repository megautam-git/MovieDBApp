package com.gs.moviedbapp.util

import android.view.View

import com.gs.moviedbapp.model.FavouriteMovie


interface RecyclerViewClickListener {

    fun onRecyclerViewItemClick(view: View, favoriteMovie: FavouriteMovie)


}