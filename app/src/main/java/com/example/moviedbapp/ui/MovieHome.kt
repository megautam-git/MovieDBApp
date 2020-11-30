package com.example.moviedbapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.example.moviedbapp.R
import com.example.moviedbapp.databinding.MovieHomeFragmentBinding
import com.example.moviedbapp.model.MovieResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieHome : Fragment(R.layout.movie_home_fragment) ,MovieAdapter.OnItemClickListener{

        private val viewModel by viewModels<MoviesViewModel>()
        private lateinit var _binding:MovieHomeFragmentBinding
        private val binding get() = _binding!!

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            _binding = MovieHomeFragmentBinding.bind(view)

            val adapter = MovieAdapter(this)

            binding.apply {
                rvMovie.setHasFixedSize(true)
                rvMovie.adapter = adapter.withLoadStateHeaderAndFooter(
                    header = MovieLoadStateAdapter {adapter.retry()},
                    footer = MovieLoadStateAdapter {adapter.retry()}
                )
                btnTryAgain.setOnClickListener {
                    adapter.retry()
                }
            }

            viewModel.movies.observe(viewLifecycleOwner){
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            }

            adapter.addLoadStateListener { loadState ->
                binding.apply {
                    progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                    rvMovie.isVisible = loadState.source.refresh is LoadState.NotLoading
                    btnTryAgain.isVisible =loadState.source.refresh is LoadState.Error
                    tvFailed.isVisible = loadState.source.refresh is LoadState.Error

                    //not found
                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        adapter.itemCount < 1){
                        rvMovie.isVisible = false
                        tvNotFound.isVisible = true
                    } else {
                        tvNotFound.isVisible = false
                    }
                }
            }

            setHasOptionsMenu(true)
        }

        override fun onItemClick(movie: MovieResult) {
            //val action = MovieFragmentDirections.actionNavMovieToNavDetails(movie)
            //findNavController().navigate(action)
        }

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            super.onCreateOptionsMenu(menu, inflater)
            inflater.inflate(R.menu.menu_search, menu)

            val searchItem = menu.findItem(R.id.action_search)
            val searchView = searchItem.actionView as SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query!=null){
                        binding.rvMovie.scrollToPosition(0)
                        viewModel.searchMovies(query)
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