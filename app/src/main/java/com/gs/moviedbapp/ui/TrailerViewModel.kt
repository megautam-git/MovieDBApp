package com.gs.moviedbapp.ui

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gs.moviedbapp.model.TrailerResult
import com.gs.moviedbapp.repositories.MovieRepository
import kotlinx.coroutines.launch

class TrailerViewModel @ViewModelInject constructor(private val repository: MovieRepository):ViewModel() {
    val _videoDetails= MutableLiveData<List<TrailerResult>>()
    val videoDetails: LiveData<List<TrailerResult>>
        get() = _videoDetails



    /*init {
        getVideoDetails()
    }*/

   suspend fun getVideoDetails(id:Int)=viewModelScope.launch{
        Log.d("getvideodetail", "getVideoDetails: its called")
        Log.d("trailerresult", "getVideoDetails: ${repository.getTrailers(id).trailerResults}")
        _videoDetails.value=repository.getTrailers(id).trailerResults

        Log.d("livedataval", "getVideoDetails:${_videoDetails.value} ")

    }
}