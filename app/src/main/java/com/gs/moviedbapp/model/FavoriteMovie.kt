package com.gs.moviedbapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "favorite_movie")
data class FavoriteMovie(
        @SerializedName("adult")
        var adult: Boolean?=false,
        @SerializedName("backdrop_path")
        var backdropPath: String?=null,
       /* @SerializedName("genre_ids")
        var genreIds: List<Int>?=null,*/
        @SerializedName("id")
        var id: Int?=0,
        @SerializedName("original_language")
        var originalLanguage: String?=null,
        @SerializedName("original_title")
        var originalTitle: String?=null,
        @SerializedName("overview")
        var overview: String?=null,
        @SerializedName("popularity")
        var popularity: Double?=null,
        @SerializedName("poster_path")
        var posterPath: String?=null,
        @SerializedName("release_date")
        var releaseDate: String?=null,
        @SerializedName("title")
        var title: String?=null,
        @SerializedName("video")
        var video: Boolean?=false,
        @SerializedName("vote_average")
        var voteAverage:Double?=0.0 ,
        @SerializedName("vote_count")
        var voteCount: Int?=0
) {
        @PrimaryKey(autoGenerate = true)
        var saved_id: Int=0
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}