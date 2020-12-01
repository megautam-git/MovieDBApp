package com.example.moviedbapp.model

import com.google.gson.annotations.SerializedName

data class Trailers(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val trailerResults: List<TrailerResult>
)