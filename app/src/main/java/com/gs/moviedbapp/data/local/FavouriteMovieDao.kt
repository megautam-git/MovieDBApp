package com.gs.moviedbapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gs.moviedbapp.model.FavouriteMovie

@Dao
interface FavouriteMovieDao {
    @Insert
    suspend fun addToFavourite(favoriteMovie: FavouriteMovie)

    @Query("SELECT * FROM favorite_movie")
    fun getFavoriteMovie(): LiveData<List<FavouriteMovie>>

    @Query("SELECT count(*) FROM favorite_movie WHERE favorite_movie.id = :id")
    suspend fun checkMovie(id: String): Int

    @Query("DELETE FROM favorite_movie WHERE favorite_movie.id = :id" )
    suspend fun removeFromFavorite(id: kotlin.Int) : Int
}