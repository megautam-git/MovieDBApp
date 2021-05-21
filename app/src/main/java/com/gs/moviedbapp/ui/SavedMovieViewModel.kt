package com.gs.moviedbapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gs.moviedbapp.data.local.FavMovieRepo
import com.gs.moviedbapp.model.FavouriteMovie
import kotlinx.coroutines.launch

class SavedMovieViewModel @ViewModelInject constructor(private val favMovieRepo: FavMovieRepo):ViewModel() {
    fun deleteMovieById(it: Int) =viewModelScope.launch {
        favMovieRepo.deleteFavMovie(it)
    }

    fun saveMoviedata(movieDetail: FavouriteMovie) =viewModelScope.launch {
        favMovieRepo.addToFavoriteMovie(movieDetail)
    }

    val savedMovie=favMovieRepo.getFavMovieData()
}