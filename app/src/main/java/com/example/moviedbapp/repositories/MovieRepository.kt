package com.example.moviedbapp.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.moviedbapp.adapter.NowPlayingMoviePaginSource
import com.example.moviedbapp.adapter.PopularMoviePagingSource
import com.example.moviedbapp.adapter.UpcomingMoviePagingSource
import com.example.moviedbapp.data.network.MovieApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val movieApi: MovieApi) {
    fun getUpcomingMovies() =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UpcomingMoviePagingSource(movieApi,null) }
        ).liveData
    fun getPopularMovies() =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PopularMoviePagingSource(movieApi,null) }
        ).liveData

    fun getNowPlyingMovies() =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NowPlayingMoviePaginSource(movieApi,null) }
        ).liveData

    fun searchUpcomingMovies(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {UpcomingMoviePagingSource(movieApi,query)}
        ).liveData

    fun searchNowPlayingMovies(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {NowPlayingMoviePaginSource(movieApi,query)}
        ).liveData
    fun searchPopularMovies(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {PopularMoviePagingSource(movieApi,query)}
        ).liveData

    suspend fun getTrailers(id:Int)=movieApi.getVideos(id)

}