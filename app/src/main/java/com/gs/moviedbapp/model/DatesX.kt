package com.gs.moviedbapp.model


import com.google.gson.annotations.SerializedName

data class DatesX(
    @SerializedName("maximum")
    val maximum: String,
    @SerializedName("minimum")
    val minimum: String
)