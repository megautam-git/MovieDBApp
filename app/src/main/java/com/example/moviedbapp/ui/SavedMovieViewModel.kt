package com.example.moviedbapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.moviedbapp.data.local.FavMovieRepo

class SavedMovieViewModel @ViewModelInject constructor(private val favMovieRepo: FavMovieRepo):ViewModel() {
    val savedMovie=favMovieRepo.getFavMovieData()
}