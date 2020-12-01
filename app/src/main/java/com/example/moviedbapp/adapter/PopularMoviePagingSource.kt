package com.example.moviedbapp.adapter

import androidx.paging.PagingSource
import com.example.moviedbapp.data.network.MovieApi
import com.example.moviedbapp.model.PopularResult
import retrofit2.HttpException
import java.io.IOException

const val STARTING_PAGE_INDEX2=1

class PopularMoviePagingSource( private val movieApi: MovieApi,
private val query:String?
): PagingSource<Int, PopularResult>() {
    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, PopularResult> {
        return try {

            val position = params.key ?: STARTING_PAGE_INDEX2
            val response = if (query!=null) movieApi.searchPopularhMovies(query,position) else movieApi.getPopularMovies(position)
            val movies = response.popularResults

            PagingSource.LoadResult.Page(
                data = movies,
                prevKey = if (position == STARTING_PAGE_INDEX2) null else position-1,
                nextKey = if (movies.isEmpty()) null else position+1
            )
        } catch (e: IOException){
            PagingSource.LoadResult.Error(e)
        } catch (e: HttpException){
            PagingSource.LoadResult.Error(e)
        }

    }
}