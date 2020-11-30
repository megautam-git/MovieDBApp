package com.example.moviedbapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.moviedbapp.repositories.MovieRepository

class MoviesViewModel @ViewModelInject constructor(private val repository: MovieRepository,
    @androidx.hilt.Assisted state: SavedStateHandle
) : ViewModel(){

    companion object{
        private const val CURRENT_QUERY = "current_query"
        private const val EMPTY_QUERY = ""
    }

    private val currentQuery = state.getLiveData(CURRENT_QUERY, EMPTY_QUERY)
    val movies = currentQuery.switchMap { query ->
        if (!query.isEmpty()){
            repository.getSearchMovies(query)
        }else{
            repository.getUpcomingMovies().cachedIn(viewModelScope)
        }
    }

    fun searchMovies(query: String){
        currentQuery.value = query
    }
}