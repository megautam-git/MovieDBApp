package com.gs.moviedbapp.adapter

import androidx.paging.PagingSource
import com.gs.moviedbapp.data.network.MovieApi
import com.gs.moviedbapp.model.UpcomingMovieResult
import retrofit2.HttpException
import java.io.IOException

const val STARTING_PAGE_INDEX=1
class UpcomingMoviePagingSource(
    private val movieApi: MovieApi,
    private val query:String?
): PagingSource<Int, UpcomingMovieResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UpcomingMovieResult> {
        return try {

            val position = params.key ?: STARTING_PAGE_INDEX
            val response = if (query!=null) movieApi.searchUpcomingMovies(query,position) else movieApi.getUpcomingMovies(position)
            val movies = response.upcomingMovieResults

            LoadResult.Page(
                data = movies,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position-1,
                nextKey = if (movies.isEmpty()) null else position+1
            )
        } catch (e: IOException){
            LoadResult.Error(e)
        } catch (e: HttpException){
            LoadResult.Error(e)
        }

    }
}