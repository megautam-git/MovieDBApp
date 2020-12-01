package com.example.moviedbapp.data.network



import com.example.moviedbapp.model.NowPlayingMovieResponse
import com.example.moviedbapp.model.PopularMovieResponse
import com.example.moviedbapp.model.Trailers
import com.example.moviedbapp.model.UpcomingMovieResponse
import com.example.moviedbapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY =Constants.key
    }

    @GET("movie/now_playing?api_key=$API_KEY")
    suspend fun getNowPlayingMovies(
        @Query("page") position: Int
    ): NowPlayingMovieResponse

    @GET("movie/upcoming?api_key=$API_KEY")
    suspend fun getUpcomingMovies(
        @Query("page") position: Int
    ): UpcomingMovieResponse

    @GET("movie/popular?api_key=$API_KEY")
    suspend fun getPopularMovies(
        @Query("page") position: Int
    ): PopularMovieResponse

    @GET("search/movie?api_key=$API_KEY")
    suspend fun searchUpcomingMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): UpcomingMovieResponse

    @GET("search/movie?api_key=$API_KEY")
    suspend fun searchPopularhMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): PopularMovieResponse

    @GET("search/movie?api_key=$API_KEY")
    suspend fun searchNowPlayinghMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): NowPlayingMovieResponse

    @GET("movie/{movie_id}/videos?api_key=$API_KEY")
    suspend fun getVideos(@Path("movie_id") id:Int): Trailers
}