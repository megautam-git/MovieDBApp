package com.gs.moviedbapp.util

import android.view.View
import com.gs.moviedbapp.model.FavoriteMovie


interface RecyclerViewClickListener {

    fun onRecyclerViewItemClick(view: View, favoriteMovie: FavoriteMovie)


}