package com.gs.moviedbapp.data.network



import com.gs.moviedbapp.BuildConfig
import com.gs.moviedbapp.model.*
import com.gs.moviedbapp.util.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }


    @GET("movie/{movie_id}/keywords?api_key=${BuildConfig.MOVIE_API_KEY}")
    suspend fun getGenre(@Path("movie_id") id:Int):Genre

    //previous call =movie/now_playing?api_key=$API_KEY

    @GET("movie/now_playing?api_key=${BuildConfig.MOVIE_API_KEY}")
    suspend fun getNowPlayingMovies(
            @Query("page") position: Int
    ): NowPlayingMovieResponse

    @GET("movie/upcoming?api_key=${BuildConfig.MOVIE_API_KEY}")
    suspend fun getUpcomingMovies(
        @Query("page") position: Int
    ): UpcomingMovieResponse

    @GET("movie/popular?api_key=${BuildConfig.MOVIE_API_KEY}")
    suspend fun getPopularMovies(
        @Query("page") position: Int
    ): PopularMovieResponse

    @GET("search/movie?api_key=${BuildConfig.MOVIE_API_KEY}")
    suspend fun searchUpcomingMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): UpcomingMovieResponse

    @GET("search/movie?api_key=${BuildConfig.MOVIE_API_KEY}")
    suspend fun searchPopularhMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): PopularMovieResponse

    @GET("search/movie?api_key=${BuildConfig.MOVIE_API_KEY}")
    suspend fun searchNowPlayinghMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): NowPlayingMovieResponse

    @GET("movie/{movie_id}/videos?api_key=${BuildConfig.MOVIE_API_KEY}")
    suspend fun getVideos(@Path("movie_id") id:Int): Trailers
}