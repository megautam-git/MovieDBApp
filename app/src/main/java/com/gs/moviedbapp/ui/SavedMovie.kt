package com.gs.moviedbapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gs.moviedbapp.R
import com.gs.moviedbapp.adapter.FavouriteAdapter
import com.gs.moviedbapp.util.RecyclerViewClickListener
import com.gs.moviedbapp.util.SwipeToDelete
import com.google.android.material.snackbar.Snackbar
import com.gs.moviedbapp.model.FavouriteMovie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import kotlinx.android.synthetic.main.fragment_saved_movie.*

@AndroidEntryPoint
class SavedMovie : Fragment(R.layout.fragment_saved_movie),FavouriteAdapter.OnItemClickCallback,RecyclerViewClickListener {

private lateinit var madapter:FavouriteAdapter
    private val viewModel by viewModels<SavedMovieViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val binding = FragmentFavoriteBinding.bind(view)



        viewModel.savedMovie.observe(viewLifecycleOwner){

            savedMovie.apply {
                setHasFixedSize(true)
                madapter= FavouriteAdapter(it,this@SavedMovie)
                 adapter=madapter
                 swipeToDelete(savedMovie)

            }
        }

/*
        madapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClick(favoriteMovie: FavoriteMovie) {

            }

        })*/
    }



    override fun onRecyclerViewItemClick(view: View, favouriteMovie: FavouriteMovie) {
        val bundle= bundleOf(
                "id" to favouriteMovie.id,
                "title" to favouriteMovie.title,
                "mainbackground" to favouriteMovie.backdropPath,
                "releasedate" to favouriteMovie.releaseDate,
                "posterpath" to favouriteMovie.posterPath,
                "overview" to favouriteMovie.overview,
                "popularity" to favouriteMovie.popularity,
                "language" to favouriteMovie.originalLanguage
        )
        //val action = FavoriteFragmentDirections.actionNavFavoriteToNavDetails(movie)
        Toast.makeText(requireContext(), "working", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_savedMovie_to_movieDetail,bundle)
    }

    override fun onItemClick(favouriteMovie: FavouriteMovie) {
        Log.d("trail", "onItemClick: ")
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = madapter.list[viewHolder.adapterPosition]
                //dashboardViewModel.deleteFromLocal(item.id!!)
                item.id?.let {

                    viewModel.deleteMovieById(it)


                }
                restoreData(viewHolder.itemView, item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }



    private fun restoreData(
            view: View,
            movieDetail: FavouriteMovie
    ) {
        Snackbar.make(view, "Deleted ${movieDetail.title}", Snackbar.LENGTH_LONG).also {
            it.apply {
                setAction("Undo") {
                    Toast.makeText(requireContext(),"hii", Toast.LENGTH_LONG).show()
                    viewModel.saveMoviedata(movieDetail)

                    //dashboardViewModel.saveArticle(article.title!!,article.description!!)
                }
                show()
            }
        }
    }

}