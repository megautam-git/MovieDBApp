package com.gs.moviedbapp.data.local

import com.gs.moviedbapp.model.FavoriteMovie
import javax.inject.Inject

class FavMovieRepo @Inject constructor(private  val favoriteMovieDao: FavoriteMovieDao) {
    suspend fun addToFavoriteMovie(favoriteMovie: FavoriteMovie)= favoriteMovieDao.addToFavorite(favoriteMovie)
    fun getFavMovieData()=favoriteMovieDao.getFavoriteMovie()
    suspend fun deleteFavMovie(id: Int)=favoriteMovieDao.removeFromFavorite(id)
}