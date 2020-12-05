package com.example.moviedbapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.moviedbapp.R
import com.example.moviedbapp.adapter.TrailerAdapter
import com.example.moviedbapp.model.FavoriteMovie
import com.example.moviedbapp.model.TrailerResult
import com.example.moviedbapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetail : Fragment(R.layout.fragment_movie_detail), TrailerAdapter.RecyclerViewClickListener {

    private val viewModel by viewModels<TrailerViewModel>()
    private val favviewModel by viewModels<MoviesViewModel>()
    private lateinit var trailerAdapter: TrailerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainposterbk=arguments?.getString("mainbackground")
        Glide.with(this@MovieDetail)
                .load("${Constants.IMG_BASE_URL}${mainposterbk}")
                .error(R.drawable.ic_error)
                .into(mainposter)
        val posterbk=arguments?.getString("posterpath")
        Glide.with(this@MovieDetail)
                .load("${Constants.IMG_BASE_URL}${posterbk}")
                .error(R.drawable.ic_error)
                .into(poster)
        val id=arguments?.getInt("id",0)
        val mtitle=arguments?.getString("title")
        val mreleasedate=arguments?.getString("releasedate")
        val mpopularity=arguments?.getDouble("popularity")
        val mlanguage=arguments?.getString("language")
        val moverview=arguments?.getString("overview")
         id?.let {
             viewModel.getVideoDetails(id)
             viewModel.videoDetails.observe(viewLifecycleOwner) {
                 trailer.apply {
                     setHasFixedSize(true)
                     trailerAdapter= TrailerAdapter(it,this@MovieDetail)
                     adapter=trailerAdapter

                 }
             }
         }
        (activity as AppCompatActivity).supportActionBar?.title = mtitle

        title.text=mtitle
        releasedate.text=mreleasedate
        popularity.text=mpopularity.toString()
        language.text=mlanguage
        overview.text=moverview
        setUpView()
        favourite.setOnClickListener(View.OnClickListener {
            val favoriteMovie=FavoriteMovie()
            favoriteMovie.id=id
            favoriteMovie.title=mtitle
            favoriteMovie.releaseDate=mreleasedate
            favoriteMovie.backdropPath=mainposterbk
            favoriteMovie.posterPath=posterbk
            favoriteMovie.popularity=mpopularity
            favoriteMovie.overview=moverview
            favoriteMovie.originalLanguage=mlanguage
            CoroutineScope(Dispatchers.IO).launch {
                favviewModel.addToSavedMovie(favoriteMovie)
            }

        })


    }

    private fun setUpView() {

    }

    override fun onItemClick(view: View, trailerResult: TrailerResult) {
    val intent = Intent(Intent.ACTION_VIEW)
    val thumbnail =
            "https://m.youtube.com/watch?v="+ trailerResult.key
    intent.data = Uri.parse(thumbnail)
    //getYoutubeVideoUri(trailer.source)
    startActivity(intent)
}




}