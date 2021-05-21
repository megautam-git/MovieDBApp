package com.gs.moviedbapp.ui

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gs.moviedbapp.data.local.FavMovieRepo
import com.gs.moviedbapp.model.FavouriteMovie
import com.gs.moviedbapp.model.Genre
import com.gs.moviedbapp.model.Keyword
import com.gs.moviedbapp.repositories.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoviesViewModel @ViewModelInject constructor(private val repository: MovieRepository,
                                                   @androidx.hilt.Assisted state: SavedStateHandle, private val favMovieRepo: FavMovieRepo
) : ViewModel() {

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val EMPTY_QUERY = ""
    }

    init {
        getGenreById(52774)

    }

    /* private var currentResult: Flow<PagingData<UpcomingMovie>>? = null
     // OBSERVABLES ---
     fun imageData(): Flow<PagingData<UpcomingMovie>> {
         val lastResult = currentResult
         if (lastResult != null) return lastResult
         val newResult: Flow<PagingData<UpcomingMovie>> =
                 repository.searchUpcomingMovies(lastResult).flow.cachedIn(viewModelScope)
         currentResult = newResult
         return newResult
     }*/
    private val _genre = MutableLiveData<List<Keyword>>()
    val genre: LiveData<List<Keyword>>
        get() = _genre
    private val currentQuery = state.getLiveData(CURRENT_QUERY, EMPTY_QUERY)
    val upcomingmovies = currentQuery.switchMap { query ->
        if (!query.isEmpty()) {
            repository.searchUpcomingMovies(query)
        } else {
            repository.getUpcomingMovies().cachedIn(viewModelScope)
        }
    }
    val nowplayingmovies = currentQuery.switchMap { query ->
        if (!query.isEmpty()) {
            repository.searchNowPlayingMovies(query)
        } else {
            repository.getNowPlyingMovies().cachedIn(viewModelScope)
        }
    }
    val popularmovies = currentQuery.switchMap { query ->
        if (!query.isEmpty()) {
            repository.searchPopularMovies(query)
        } else {
            repository.getPopularMovies().cachedIn(viewModelScope)
        }
    }

    fun searchUpcomingMovies(query: String) {
        currentQuery.value = query
    }

    fun searchPopularMovies(query: String) {
        currentQuery.value = query
    }

    fun searchNowplayingMovies(query: String) {
        currentQuery.value = query
    }

    fun getGenreById(id: Int) = viewModelScope.launch(Dispatchers.Main) {
        _genre.value = repository.getGenre(id).keywords
        Log.d("genre data", "${_genre.value.toString()}")
    }

    suspend fun addToSavedMovie(favoriteMovie: FavouriteMovie) = CoroutineScope(Dispatchers.IO).launch {
        favMovieRepo.addToFavoriteMovie(favoriteMovie)
    }
}