package com.example.moviedbapp.model


import com.google.gson.annotations.SerializedName

data class UpcomingMovieResponse(
    @SerializedName("dates")
    val dates: Dates,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val upcomingMovieResults: List<UpcomingMovieResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)