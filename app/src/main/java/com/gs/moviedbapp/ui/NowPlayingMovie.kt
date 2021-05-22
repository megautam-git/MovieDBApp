package com.gs.moviedbapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.gs.moviedbapp.R
import com.gs.moviedbapp.adapter.MovieLoadStateAdapter
import com.gs.moviedbapp.adapter.NowPlayingMovieAdapter
import com.gs.moviedbapp.databinding.FragmentNowPlayingMovieBinding
import com.gs.moviedbapp.model.NowPlayingResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class NowPlayingMovie() : Fragment(R.layout.fragment_now_playing_movie) , NowPlayingMovieAdapter.OnItemClickListener{

    private val viewModel by viewModels<MoviesViewModel>()
    private lateinit var _binding:FragmentNowPlayingMovieBinding
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNowPlayingMovieBinding.bind(view)

        val adapter = NowPlayingMovieAdapter(this)

        binding.apply {
            nowPlaying.setHasFixedSize(true)
            nowPlaying.adapter = adapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter {adapter.retry()},
                footer = MovieLoadStateAdapter {adapter.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.nowplayingmovies.observe(viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                nowPlaying.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnTryAgain.isVisible =loadState.source.refresh is LoadState.Error
                tvFailed.isVisible = loadState.source.refresh is LoadState.Error

                //not found
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1){
                    nowPlaying.isVisible = false
                    tvNotFound.isVisible = true
                } else {
                    tvNotFound.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemClick(nowPlayingResult: NowPlayingResult) {
        viewModel.getGenreById(nowPlayingResult.id)

        val bundle= bundleOf(
                "id" to nowPlayingResult.id,
                "title" to nowPlayingResult.title,
                "mainbackground" to nowPlayingResult.backdropPath,
                "releasedate" to nowPlayingResult.releaseDate,
                "posterpath" to nowPlayingResult.posterPath,
                "overview" to nowPlayingResult.overview,
                "popularity" to nowPlayingResult.popularity,
            "vote" to nowPlayingResult.voteAverage,
                "language" to nowPlayingResult.originalLanguage
        )


        findNavController().navigate(R.id.action_nowPlayingMovie_to_movieDetail,bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!=null){
                    binding.nowPlaying.scrollToPosition(0)
                    viewModel.searchNowplayingMovies(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }


}