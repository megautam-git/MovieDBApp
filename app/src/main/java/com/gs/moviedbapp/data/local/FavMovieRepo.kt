package com.gs.moviedbapp.data.local


import com.gs.moviedbapp.model.FavouriteMovie
import javax.inject.Inject

class FavMovieRepo @Inject constructor(private  val favouriteMovieDao: FavouriteMovieDao) {
    suspend fun addToFavoriteMovie(favouriteMovie: FavouriteMovie)= favouriteMovieDao.addToFavourite(favouriteMovie)
    fun getFavMovieData()=favouriteMovieDao.getFavoriteMovie()
    suspend fun deleteFavMovie(id: Int)=favouriteMovieDao.removeFromFavorite(id)
}