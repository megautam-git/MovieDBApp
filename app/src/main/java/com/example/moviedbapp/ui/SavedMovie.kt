package com.example.moviedbapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.moviedbapp.R
import com.example.moviedbapp.adapter.FavoriteAdapter
import com.example.moviedbapp.model.FavoriteMovie
import com.example.moviedbapp.util.RecyclerViewClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_saved_movie.*

@AndroidEntryPoint
class SavedMovie : Fragment(R.layout.fragment_saved_movie),FavoriteAdapter.OnItemClickCallback,RecyclerViewClickListener {

private lateinit var madapter:FavoriteAdapter
    private val viewModel by viewModels<SavedMovieViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val binding = FragmentFavoriteBinding.bind(view)



        viewModel.savedMovie.observe(viewLifecycleOwner){

            savedMovie.apply {
                setHasFixedSize(true)
                madapter= FavoriteAdapter(it,this@SavedMovie)
                 adapter=madapter

            }
        }

/*
        madapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClick(favoriteMovie: FavoriteMovie) {

            }

        })*/
    }



    override fun onRecyclerViewItemClick(view: View, favoriteMovie: FavoriteMovie) {
        val bundle= bundleOf(
                "id" to favoriteMovie.id,
                "title" to favoriteMovie.title,
                "mainbackground" to favoriteMovie.backdropPath,
                "releasedate" to favoriteMovie.releaseDate,
                "posterpath" to favoriteMovie.posterPath,
                "overview" to favoriteMovie.overview,
                "popularity" to favoriteMovie.popularity,
                "language" to favoriteMovie.originalLanguage
        )
        //val action = FavoriteFragmentDirections.actionNavFavoriteToNavDetails(movie)
        Toast.makeText(requireContext(), "working", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_savedMovie_to_movieDetail,bundle)
    }

    override fun onItemClick(favoriteMovie: FavoriteMovie) {
        Log.d("trail", "onItemClick: ")
    }


}