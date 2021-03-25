package com.gs.moviedbapp.model


import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("keywords")
    val keywords: List<Keyword>
)