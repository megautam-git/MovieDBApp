package com.example.moviedbapp.adapter

import androidx.paging.PagingSource
import com.example.moviedbapp.data.network.MovieApi
import com.example.moviedbapp.model.NowPlayingResult
import retrofit2.HttpException
import java.io.IOException

const val STARTING_PAGE_INDEX1=1
class NowPlayingMoviePaginSource(private val movieApi: MovieApi,
                                 private val query:String?
): PagingSource<Int, NowPlayingResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NowPlayingResult> {
        return try {

            val position = params.key ?: STARTING_PAGE_INDEX1
            val response = if (query!=null) movieApi.searchNowPlayinghMovies(query,position) else movieApi.getNowPlayingMovies(position)
            val movies = response.nowPlayingResults

            LoadResult.Page(
                data = movies,
                prevKey = if (position == STARTING_PAGE_INDEX1) null else position-1,
                nextKey = if (movies.isEmpty()) null else position+1
            )
        } catch (e: IOException){
            LoadResult.Error(e)
        } catch (e: HttpException){
            LoadResult.Error(e)
        }

    }
}