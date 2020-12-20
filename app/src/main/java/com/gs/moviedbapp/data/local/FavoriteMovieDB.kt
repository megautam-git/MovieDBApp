package com.gs.moviedbapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gs.moviedbapp.model.FavoriteMovie

@Database(
        entities = [FavoriteMovie::class],
        version = 1
)
abstract class FavoriteMovieDB:RoomDatabase() {
  abstract fun getFavoriteMovieDao():FavoriteMovieDao

}