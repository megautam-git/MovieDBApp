package com.gs.moviedbapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.gs.moviedbapp.R
import com.gs.moviedbapp.adapter.MovieLoadStateAdapter
import com.gs.moviedbapp.adapter.UpcomingMovieAdapter
import com.gs.moviedbapp.databinding.FragmentUpcomingMovieBinding
import com.gs.moviedbapp.model.UpcomingMovieResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_now_playing_movie.*
import kotlinx.android.synthetic.main.fragment_upcoming_movie.*

// TODO: Rename parameter arguments, choose names that match
@AndroidEntryPoint
class UpcomingMovie : Fragment(R.layout.fragment_upcoming_movie) , UpcomingMovieAdapter.OnItemClickListener{

    private val viewModel by viewModels<MoviesViewModel>()
    private  var _binding:FragmentUpcomingMovieBinding?=null
    private val binding get() = _binding!!
    private  var adapter: UpcomingMovieAdapter?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentUpcomingMovieBinding.bind(view)

         adapter = UpcomingMovieAdapter(this)

        binding.apply {
            upcoming.setHasFixedSize(true)
            upcoming.adapter = adapter!!.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter {adapter!!.retry()},
                footer = MovieLoadStateAdapter {adapter!!.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter!!.retry()
            }
        }

        viewModel.upcomingmovies.observe(viewLifecycleOwner){
            adapter!!.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter!!.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                upcoming.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnTryAgain.isVisible =loadState.source.refresh is LoadState.Error
                tvFailed.isVisible = loadState.source.refresh is LoadState.Error

                //not found
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter!!.itemCount < 1){
                    upcoming.isVisible = false
                    tvNotFound.isVisible = true
                } else {
                    tvNotFound.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemClick(upcomingMovie: UpcomingMovieResult) {
        val bundle= bundleOf(
                "id" to upcomingMovie.id,
                "title" to upcomingMovie.title,
                "mainbackground" to upcomingMovie.backdropPath,
                "releasedate" to upcomingMovie.releaseDate,
                "posterpath" to upcomingMovie.posterPath,
                "overview" to upcomingMovie.overview,
                "popularity" to upcomingMovie.popularity,
                "vote" to upcomingMovie.voteAverage,
                "language" to upcomingMovie.originalLanguage
        )


        findNavController().navigate(R.id.action_upcomingMovie_to_movieDetail,bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!=null){
                    binding.upcoming.scrollToPosition(0)
                    viewModel.searchUpcomingMovies(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
        upcoming.adapter=null
        adapter=null
    }

}