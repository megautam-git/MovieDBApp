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
import com.bumptech.glide.Glide
import com.gs.moviedbapp.R
import com.gs.moviedbapp.adapter.TrailerAdapter
import com.gs.moviedbapp.model.FavoriteMovie
import com.gs.moviedbapp.model.TrailerResult
import com.gs.moviedbapp.util.Constants
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



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//notch
      /* *//* val attrib =  (activity as AppCompatActivity).window.attributes
        attrib.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        attrib.flags=WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS*//*
        requireActivity().window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        //(activity as AppCompatActivity).window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        (activity as AppCompatActivity).window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        cutoutSupport()*/


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

        title.text=mtitle
        releasedate.text=mreleasedate
        popularity.text=mpopularity.toString()
        language.text=mlanguage
        overview.text=moverview
        setUpView()
        favourite.setOnClickListener(View.OnClickListener {
            val favoriteMovie = FavoriteMovie()
            favoriteMovie.id = id
            favoriteMovie.title = mtitle
            favoriteMovie.releaseDate = mreleasedate
            favoriteMovie.backdropPath = mainposterbk
            favoriteMovie.posterPath = posterbk
            favoriteMovie.popularity = mpopularity
            favoriteMovie.overview = moverview
            favoriteMovie.originalLanguage = mlanguage
            CoroutineScope(Dispatchers.IO).launch {
                favviewModel.addToSavedMovie(favoriteMovie)
            }

        })


    }

    private fun cutoutSupport() {
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window.ser(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }*/
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