package com.example.moviedbapp.adapter

import androidx.paging.PagingSource
import com.example.moviedbapp.data.network.MovieApi
import com.example.moviedbapp.model.MovieResult
import retrofit2.HttpException
import java.io.IOException

const val STARTING_PAGE_INDEX=1
class MoviePagingSource(
    private val movieApi: MovieApi,
    private val query:String?
): PagingSource<Int, MovieResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResult> {
        return try {

            val position = params.key ?: STARTING_PAGE_INDEX
            val response = if (query!=null) movieApi.searchMovies(query,position) else movieApi.getNowPlayingMovies(position)
            val movies = response.movieResults

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