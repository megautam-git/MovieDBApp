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
import com.gs.moviedbapp.adapter.PopularMovieAdapter
import com.gs.moviedbapp.databinding.FragmentPopularMovieBinding
import com.gs.moviedbapp.model.PopularResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularMovie : Fragment(R.layout.fragment_popular_movie)  , PopularMovieAdapter.OnItemClickListener{

    private val viewModel by viewModels<MoviesViewModel>()
    private lateinit var _binding: FragmentPopularMovieBinding
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPopularMovieBinding.bind(view)

        val adapter = PopularMovieAdapter(this)

        binding.apply {
            popular.setHasFixedSize(true)
            popular.adapter = adapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter {adapter.retry()},
                footer = MovieLoadStateAdapter {adapter.retry()}
            )
            btnTryAgain.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.popularmovies.observe(viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                popular.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnTryAgain.isVisible =loadState.source.refresh is LoadState.Error
                tvFailed.isVisible = loadState.source.refresh is LoadState.Error

                //not found
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1){
                    popular.isVisible = false
                    tvNotFound.isVisible = true
                } else {
                    tvNotFound.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemClick(popularResult: PopularResult) {
        val bundle= bundleOf(
                "id" to popularResult.id,
                "title" to popularResult.title,
                "mainbackground" to popularResult.backdropPath,
                "releasedate" to popularResult.releaseDate,
                "posterpath" to popularResult.posterPath,
                "overview" to popularResult.overview,
                "popularity" to popularResult.popularity,
                "language" to popularResult.originalLanguage
        )


        findNavController().navigate(R.id.action_popularMovie_to_movieDetail,bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!=null){
                    binding.popular.scrollToPosition(0)
                    viewModel.searchPopularMovies(query)
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