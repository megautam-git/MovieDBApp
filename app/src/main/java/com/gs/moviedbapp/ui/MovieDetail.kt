package com.gs.moviedbapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gs.moviedbapp.R
import com.gs.moviedbapp.adapter.TrailerAdapter
import com.gs.moviedbapp.model.FavouriteMovie
import com.gs.moviedbapp.model.TrailerResult
import com.gs.moviedbapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import kotlinx.android.synthetic.main.fragment_upcoming_movie.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetail : Fragment(R.layout.fragment_movie_detail), TrailerAdapter.RecyclerViewClickListener {

    private val viewModel by viewModels<TrailerViewModel>()
    private val favviewModel by viewModels<MoviesViewModel>()
    private lateinit var trailerAdapter: TrailerAdapter



    @RequiresApi(Build.VERSION_CODES.P)
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
        val id=arguments?.getInt("id", 0)
        val mtitle=arguments?.getString("title")
        val mreleasedate=arguments?.getString("releasedate")
        val mpopularity=arguments?.getDouble("popularity")
        val mlanguage=arguments?.getString("language")
        val moverview=arguments?.getString("overview")
        val mvote=arguments?.getDouble("vote")
         id?.let {
             CoroutineScope(Dispatchers.Main).launch {
                 viewModel.getVideoDetails(id)
             }

             viewModel.videoDetails.observe(viewLifecycleOwner) {
                 trailer.apply {
                     setHasFixedSize(true)
                     trailerAdapter= TrailerAdapter(it, this@MovieDetail)
                     adapter=trailerAdapter

                 }
             }
         }
        //(activity as AppCompatActivity).supportActionBar?.title = mtitle

        //(activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title ="Movie DB"
        title.text=mtitle
        releasedate.text=mreleasedate
        popularity.text=mvote.toString()
        language.text=mlanguage
        overview.text=moverview
        setUpView()
        favourite.setOnClickListener(View.OnClickListener {
            val favouriteMovie = FavouriteMovie()
            favouriteMovie.id = id
            favouriteMovie.title = mtitle
            favouriteMovie.releaseDate = mreleasedate
            favouriteMovie.backdropPath = mainposterbk
            favouriteMovie.posterPath = posterbk
            favouriteMovie.popularity = mpopularity
            favouriteMovie.overview = moverview
            favouriteMovie.originalLanguage = mlanguage
            CoroutineScope(Dispatchers.IO).launch {
                favviewModel.addToSavedMovie(favouriteMovie)
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