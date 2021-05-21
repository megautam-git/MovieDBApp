package com.gs.moviedbapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gs.moviedbapp.model.FavouriteMovie

@Database(
        entities = [FavouriteMovie::class],
        version = 1,
    exportSchema = false
)
abstract class FavouriteMovieDB:RoomDatabase() {
  abstract fun getFavouriteMovieDao():FavouriteMovieDao

}