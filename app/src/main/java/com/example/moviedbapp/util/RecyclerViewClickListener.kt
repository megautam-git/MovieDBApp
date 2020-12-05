package com.example.moviedbapp.util

import android.view.View
import com.example.moviedbapp.model.FavoriteMovie


interface RecyclerViewClickListener {

    fun onRecyclerViewItemClick(view: View, favoriteMovie: FavoriteMovie)


}