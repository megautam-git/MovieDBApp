package com.gs.moviedbapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.gs.moviedbapp.R
import com.gs.moviedbapp.adapter.MovieLoadStateAdapter
import com.gs.moviedbapp.adapter.NowPlayingMovieAdapter
import com.gs.moviedbapp.adapter.PopularMovieAdapter
import com.gs.moviedbapp.adapter.UpcomingMovieAdapter
import com.gs.moviedbapp.databinding.MovieHomeFragmentBinding
import com.gs.moviedbapp.model.NowPlayingResult
import com.gs.moviedbapp.model.PopularResult
import com.gs.moviedbapp.model.UpcomingMovieResult
import com.gs.moviedbapp.util.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.movie_home_fragment.*


@AndroidEntryPoint
@SuppressLint("ValidFragment")
class MovieHome : Fragment(R.layout.movie_home_fragment), UpcomingMovieAdapter.OnItemClickListener,
    NowPlayingMovieAdapter.OnItemClickListener, PopularMovieAdapter.OnItemClickListener,
    View.OnClickListener {
    private lateinit var adapter: UpcomingMovieAdapter
    private lateinit var adapter1: NowPlayingMovieAdapter
    private lateinit var adapter2: PopularMovieAdapter
    private val viewModel by viewModels<MoviesViewModel>()
    private lateinit var _binding: MovieHomeFragmentBinding
    private val binding get() = _binding!!
    private var bacckpresstime: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {
                   /* val mytoast =
                        Toast.makeText(context, "press again to exit..", Toast.LENGTH_SHORT)
                    //doubleBackToExitPressedOnce=true
                    if (bacckpresstime + 2000 > System.currentTimeMillis()) {
                        mytoast.cancel()
                        Handler().postDelayed({
                            requireActivity().finish()
                        }, 1500)

                        return
                    } else {
                        mytoast.show()
                    }
                    if (requireActivity().isFinishing) {
                        mytoast.cancel()
                    }
                    //view?.let { Snackbar.make(it,"press again to exit",Snackbar.LENGTH_SHORT).show() }}
                    bacckpresstime = System.currentTimeMillis()*/
                    val snackbar=Snackbar.make(view!!,"exit?",Snackbar.LENGTH_SHORT)
                    snackbar.setActionTextColor(Color.WHITE)
                    snackbar.setBackgroundTint(Color.BLACK)
                    snackbar.setTextColor(Color.RED)
                    snackbar.setAction("yes", View.OnClickListener {
                        requireActivity().finish()
                    })
                          snackbar.show()

                    /* Handler().postDelayed({
                            //  doubleBackToExitPressedOnce=false

                             requireActivity().finish()

                         }


                         ,3000
                     )*/


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        upcoming_id.setOnClickListener(this)
        nowplaying_id.setOnClickListener(this)
        popular_id.setOnClickListener(this)
        _binding = MovieHomeFragmentBinding.bind(view)
        (activity as AppCompatActivity).supportActionBar?.title = "Movie DB"

        /* val wifi_service = context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activenetwork = wifi_service.activeNetworkInfo
        if (activenetwork != null && activenetwork.isConnected) {
            if (activenetwork.type == ConnectivityManager.TYPE_MOBILE) {

                Toast.makeText(context, "connected to mobile", Toast.LENGTH_LONG).show()
            }
            if (activenetwork.type == ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(context, "connected  to wifi", Toast.LENGTH_LONG).show()
            }

        } else {
            val snackBar = Snackbar.make(
                view, "Replace with your own action",
                Snackbar.LENGTH_LONG
            ).setAction("Action", null)
            snackBar.setActionTextColor(Color.BLUE)
            val snackBarView = snackBar.view
            snackBarView.setBackgroundColor(Color.CYAN)
            val textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.BLUE)
            textView.setOnClickListener(View.OnClickListener {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting")
                startActivity(intent)
            })
            snackBar.show()
        }*/

        val networkConnection = context?.let { NetworkConnection(it) }
        networkConnection!!.observe(viewLifecycleOwner, Observer { isConnected ->
            if (isConnected) {
                myview.visibility = View.VISIBLE
                adapter = UpcomingMovieAdapter(this)
                adapter1 = NowPlayingMovieAdapter(this)
                adapter2 = PopularMovieAdapter(this)

                binding.apply {
                    upcomingmovieview.setHasFixedSize(true)
                    upcomingmovieview.adapter = adapter.withLoadStateHeaderAndFooter(
                        header = MovieLoadStateAdapter { adapter.retry() },
                        footer = MovieLoadStateAdapter { adapter.retry() }
                    )
                    nowplayingview.setHasFixedSize(true)
                    nowplayingview.adapter = adapter1.withLoadStateHeaderAndFooter(
                        header = MovieLoadStateAdapter { adapter1.retry() },
                        footer = MovieLoadStateAdapter { adapter1.retry() }
                    )
                    popularMovieView.setHasFixedSize(true)
                    popularMovieView.adapter = adapter2.withLoadStateHeaderAndFooter(
                        header = MovieLoadStateAdapter { adapter2.retry() },
                        footer = MovieLoadStateAdapter { adapter2.retry() }
                    )


                    /*btnTryAgain.setOnClickListener {
                        adapter.retry()
                    }*/
                }

                viewModel.upcomingmovies.observe(viewLifecycleOwner) {
                    adapter.submitData(viewLifecycleOwner.lifecycle, it)
                }
                viewModel.nowplayingmovies.observe(viewLifecycleOwner) {
                    adapter1.submitData(viewLifecycleOwner.lifecycle, it)
                }
                viewModel.popularmovies.observe(viewLifecycleOwner) {
                    adapter2.submitData(viewLifecycleOwner.lifecycle, it)
                }
                adapter.addLoadStateListener { loadState ->
                    binding.apply {
                        progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                        upcomingmovieview.isVisible =
                            loadState.source.refresh is LoadState.NotLoading
                        /*btnTryAgain.isVisible =loadState.source.refresh is LoadState.Error
                        tvFailed.isVisible = loadState.source.refresh is LoadState.Error
    */
                        //not found
                        if (loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached &&
                            adapter.itemCount < 1
                        ) {
                            upcomingmovieview.isVisible = false
                            /* tvNotFound.isVisible = true*/
                        } else {
                            /*tvNotFound.isVisible = false*/
                        }
                    }
                }

                adapter1.addLoadStateListener { loadState ->
                    binding.apply {
                        progressBar2.isVisible = loadState.source.refresh is LoadState.Loading
                        nowplayingview.isVisible = loadState.source.refresh is LoadState.NotLoading
                        /*btnTryAgain.isVisible =loadState.source.refresh is LoadState.Error
                        tvFailed.isVisible = loadState.source.refresh is LoadState.Error
    */
                        //not found
                        if (loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached &&
                            adapter.itemCount < 1
                        ) {
                            nowplayingview.isVisible = false
                            /* tvNotFound.isVisible = true*/
                        } else {
                            /*tvNotFound.isVisible = false*/
                        }
                    }
                }

                adapter2.addLoadStateListener { loadState ->
                    binding.apply {
                        progressBar1.isVisible = loadState.source.refresh is LoadState.Loading
                        popularMovieView.isVisible =
                            loadState.source.refresh is LoadState.NotLoading
                        /*btnTryAgain.isVisible =loadState.source.refresh is LoadState.Error
                        tvFailed.isVisible = loadState.source.refresh is LoadState.Error
    */
                        //not found
                        if (loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached &&
                            adapter.itemCount < 1
                        ) {
                            popularMovieView.isVisible = false
                            /* tvNotFound.isVisible = true*/
                        } else {
                            /*tvNotFound.isVisible = false*/
                        }
                    }
                }

            } else {
                myview.visibility = View.INVISIBLE
                val snackBar = Snackbar.make(
                    view, "Not Connected...",
                    Snackbar.LENGTH_LONG
                ).setAction("Connect", View.OnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        startActivityForResult(
                            Intent(
                                Settings.Panel.ACTION_INTERNET_CONNECTIVITY
                            ), 545
                        )
                    }
                }).show()

            }
        })




        setHasOptionsMenu(true)
    }

    override fun onItemClick(upcomingMovie: UpcomingMovieResult) {
        Toast.makeText(requireContext(), "working", Toast.LENGTH_LONG).show()
        //trailerViewModdel.getVideoDetails(topRatedResult.id)
        //Log.d("topratedvalue:", "${topRatedResult.id.toLong()}")
        val bundle = bundleOf(
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

        /*  val action=UpcomingMovieDirections.actionUpcomingMovieToMovieDetail()
          findNavController().navigate(action)*/
        findNavController().navigate(R.id.action_movieHome_to_movieDetail, bundle)
    }


    override fun onItemClick(nowPlayingResult: NowPlayingResult) {
        Toast.makeText(requireContext(), "working", Toast.LENGTH_LONG).show()
        //trailerViewModdel.getVideoDetails(topRatedResult.id)
        //Log.d("topratedvalue:", "${topRatedResult.id.toLong()}")
        val bundle = bundleOf(
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


        findNavController().navigate(R.id.action_movieHome_to_movieDetail, bundle)
    }

    override fun onItemClick(popularmovieresult: PopularResult) {
        Toast.makeText(requireContext(), "working", Toast.LENGTH_LONG).show()
        //trailerViewModdel.getVideoDetails(topRatedResult.id)
        //Log.d("topratedvalue:", "${topRatedResult.id.toLong()}")
        val bundle = bundleOf(
            "id" to popularmovieresult.id,
            "title" to popularmovieresult.title,
            "mainbackground" to popularmovieresult.backdropPath,
            "releasedate" to popularmovieresult.releaseDate,
            "posterpath" to popularmovieresult.posterPath,
            "overview" to popularmovieresult.overview,
            "popularity" to popularmovieresult.popularity,
            "vote" to popularmovieresult.voteAverage,
            "language" to popularmovieresult.originalLanguage
        )


        findNavController().navigate(R.id.action_movieHome_to_movieDetail, bundle)
    }

    override fun onClick(v: View?) {
        val id = v!!.id
        when (id) {
            R.id.upcoming_id -> {
                findNavController().navigate(R.id.action_movieHome_to_upcomingMovie)
            }
            R.id.nowplaying_id -> {
                findNavController().navigate(R.id.action_movieHome_to_nowPlayingMovie)
            }
            R.id.popular_id -> {
                findNavController().navigate(R.id.action_movieHome_to_popularMovie)
            }

            else -> {
                Log.d("wrong id", "onClick: ")
            }
        }

    }


}