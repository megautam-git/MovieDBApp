package com.gs.moviedbapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gs.moviedbapp.data.local.FavMovieRepo
import com.gs.moviedbapp.model.FavoriteMovie
import com.gs.moviedbapp.repositories.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel @ViewModelInject constructor(private val repository: MovieRepository,
    @androidx.hilt.Assisted state: SavedStateHandle,private val favMovieRepo: FavMovieRepo
) : ViewModel(){

    companion object{
        private const val CURRENT_QUERY = "current_query"
        private const val EMPTY_QUERY = ""
    }

    private val currentQuery = state.getLiveData(CURRENT_QUERY, EMPTY_QUERY)
    val upcomingmovies = currentQuery.switchMap { query ->
        if (!query.isEmpty()){
            repository.searchUpcomingMovies(query)
        }else{
            repository.getUpcomingMovies().cachedIn(viewModelScope)
        }
    }
    val nowplayingmovies = currentQuery.switchMap { query ->
        if (!query.isEmpty()){
            repository.searchNowPlayingMovies(query)
        }else{
            repository.getNowPlyingMovies().cachedIn(viewModelScope)
        }
    }
    val popularmovies = currentQuery.switchMap { query ->
        if (!query.isEmpty()){
            repository.searchPopularMovies(query)
        }else{
            repository.getPopularMovies().cachedIn(viewModelScope)
        }
    }

    fun searchUpcomingMovies(query: String){
        currentQuery.value = query
    }
    fun searchPopularMovies(query: String){
        currentQuery.value = query
    }
    fun searchNowplayingMovies(query: String){
        currentQuery.value = query
    }


    suspend fun addToSavedMovie(favoriteMovie: FavoriteMovie)= CoroutineScope(Dispatchers.IO).launch {
        favMovieRepo.addToFavoriteMovie(favoriteMovie)
    }
}