package com.example.moviedbapp.model


import com.google.gson.annotations.SerializedName

data class NowPlayingMovieResponse(
    @SerializedName("dates")
    val dates: DatesX,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val nowPlayingResults: List<NowPlayingResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)