package com.gs.moviedbapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.gs.moviedbapp.R
import com.gs.moviedbapp.adapter.MovieLoadStateAdapter
import com.gs.moviedbapp.adapter.NowPlayingMovieAdapter
import com.gs.moviedbapp.adapter.PopularMovieAdapter
import com.gs.moviedbapp.adapter.UpcomingMovieAdapter
import com.gs.moviedbapp.databinding.MovieHomeFragmentBinding
import com.gs.moviedbapp.model.NowPlayingResult
import com.gs.moviedbapp.model.PopularResult
import com.gs.moviedbapp.model.UpcomingMovieResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.movie_home_fragment.*

@AndroidEntryPoint
@SuppressLint("ValidFragment")
class MovieHome : Fragment(R.layout.movie_home_fragment) , UpcomingMovieAdapter.OnItemClickListener, NowPlayingMovieAdapter.OnItemClickListener
, PopularMovieAdapter.OnItemClickListener,View.OnClickListener{

        private val viewModel by viewModels<MoviesViewModel>()
        private lateinit var _binding:MovieHomeFragmentBinding
        private val binding get() = _binding!!

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
              upcoming_id.setOnClickListener(this)
              nowplaying_id.setOnClickListener(this)
              popular_id.setOnClickListener(this)
            _binding = MovieHomeFragmentBinding.bind(view)
            (activity as AppCompatActivity).supportActionBar?.title = "Movie DB"
            val adapter = UpcomingMovieAdapter(this)
            val adapter1 = NowPlayingMovieAdapter(this)
            val adapter2 = PopularMovieAdapter(this)

            binding.apply {
                upcomingmovieview.setHasFixedSize(true)
                upcomingmovieview.adapter = adapter.withLoadStateHeaderAndFooter(
                    header = MovieLoadStateAdapter {adapter.retry()},
                    footer = MovieLoadStateAdapter {adapter.retry()}
                )
                nowplayingview.setHasFixedSize(true)
                nowplayingview.adapter = adapter1.withLoadStateHeaderAndFooter(
                    header = MovieLoadStateAdapter {adapter1.retry()},
                    footer = MovieLoadStateAdapter {adapter1.retry()}
                )
                popularMovieView.setHasFixedSize(true)
                popularMovieView.adapter = adapter2.withLoadStateHeaderAndFooter(
                    header = MovieLoadStateAdapter {adapter2.retry()},
                    footer = MovieLoadStateAdapter {adapter2.retry()}
                )


                /*btnTryAgain.setOnClickListener {
                    adapter.retry()
                }*/
            }

            viewModel.upcomingmovies.observe(viewLifecycleOwner){
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
            viewModel.nowplayingmovies.observe(viewLifecycleOwner){
                adapter1.submitData(viewLifecycleOwner.lifecycle, it)
            }
            viewModel.popularmovies.observe(viewLifecycleOwner){
                adapter2.submitData(viewLifecycleOwner.lifecycle, it)
            }

            adapter.addLoadStateListener { loadState ->
                binding.apply {
                    progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                    upcomingmovieview.isVisible = loadState.source.refresh is LoadState.NotLoading
                    /*btnTryAgain.isVisible =loadState.source.refresh is LoadState.Error
                    tvFailed.isVisible = loadState.source.refresh is LoadState.Error
*/
                    //not found
                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        adapter.itemCount < 1){
                        upcomingmovieview.isVisible = false
                       /* tvNotFound.isVisible = true*/
                    } else {
                        /*tvNotFound.isVisible = false*/
                    }
                }
            }

            setHasOptionsMenu(true)
        }

        override fun onItemClick(upcomingMovie: UpcomingMovieResult) {
            Toast.makeText(requireContext(),"working", Toast.LENGTH_LONG).show()
            //trailerViewModdel.getVideoDetails(topRatedResult.id)
            //Log.d("topratedvalue:", "${topRatedResult.id.toLong()}")
            val bundle= bundleOf(
                    "id" to upcomingMovie.id,
                    "title" to upcomingMovie.title,
                    "mainbackground" to upcomingMovie.backdropPath,
                    "releasedate" to upcomingMovie.releaseDate,
                    "posterpath" to upcomingMovie.posterPath,
                    "overview" to upcomingMovie.overview,
                    "popularity" to upcomingMovie.popularity,
                    "language" to upcomingMovie.originalLanguage
            )


            findNavController().navigate(R.id.action_movieHome_to_movieDetail,bundle)
        }



    override fun onItemClick(nowPlayingResult: NowPlayingResult) {
        Toast.makeText(requireContext(),"working", Toast.LENGTH_LONG).show()
        //trailerViewModdel.getVideoDetails(topRatedResult.id)
        //Log.d("topratedvalue:", "${topRatedResult.id.toLong()}")
        val bundle= bundleOf(
                "id" to nowPlayingResult.id,
                "title" to nowPlayingResult.title,
                "mainbackground" to nowPlayingResult.backdropPath,
                "releasedate" to nowPlayingResult.releaseDate,
                "posterpath" to nowPlayingResult.posterPath,
                "overview" to nowPlayingResult.overview,
                "popularity" to nowPlayingResult.popularity,
                "language" to nowPlayingResult.originalLanguage
        )


        findNavController().navigate(R.id.action_movieHome_to_movieDetail,bundle)
    }

    override fun onItemClick(popularmovieresult: PopularResult) {
        Toast.makeText(requireContext(),"working", Toast.LENGTH_LONG).show()
        //trailerViewModdel.getVideoDetails(topRatedResult.id)
        //Log.d("topratedvalue:", "${topRatedResult.id.toLong()}")
        val bundle= bundleOf(
                "id" to popularmovieresult.id,
                "title" to popularmovieresult.title,
                "mainbackground" to popularmovieresult.backdropPath,
                "releasedate" to popularmovieresult.releaseDate,
                "posterpath" to popularmovieresult.posterPath,
                "overview" to popularmovieresult.overview,
                "popularity" to popularmovieresult.popularity,
                "language" to popularmovieresult.originalLanguage
        )


        findNavController().navigate(R.id.action_movieHome_to_movieDetail,bundle)
    }

    override fun onClick(v: View?) {
    val id=v!!.id
        when(id){
            R.id.upcoming_id->{
               findNavController().navigate(R.id.action_movieHome_to_upcomingMovie)
            }
            R.id.nowplaying_id->{
                findNavController().navigate(R.id.action_movieHome_to_nowPlayingMovie)
            }
            R.id.popular_id->{
                findNavController().navigate(R.id.action_movieHome_to_popularMovie)
            }

            else->{
                Log.d("wrong id", "onClick: ")
            }
        }

    }


}