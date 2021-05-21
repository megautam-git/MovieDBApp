package com.gs.moviedbapp.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.*
import android.net.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData

class NetworkConnection(private val context: Context):LiveData<Boolean>() {

    private  var connectivityManager:ConnectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var newtworkCallback:ConnectivityManager.NetworkCallback

    @SuppressLint("ObsoleteSdkInt")
    override fun onActive() {
        super.onActive()
        updateConnection()
        when{
            Build.VERSION.SDK_INT>=Build.VERSION_CODES.N->{
                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallBack())

            }
            Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP->{
               lollipopNetworkRequest()

            }else->{
                context.registerReceiver(
                    networkReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }
        }

    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            try {
                /*mConnectivityManager.unregisterNetworkCallback(mNetworkCallback)*/
                connectivityManager.unregisterNetworkCallback(connectivityManagerCallBack())
            } catch (e: Exception) {
                Log.d("msg", "unregister failed")
            }


        }else{
            context.unregisterReceiver(networkReceiver)

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lollipopNetworkRequest() {
        val requestBuilder=NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        connectivityManager.registerNetworkCallback(
            requestBuilder.build(),
            connectivityManagerCallBack()
        )
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun connectivityManagerCallBack():ConnectivityManager.NetworkCallback{
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            newtworkCallback=object :ConnectivityManager.NetworkCallback(){
                override fun onLost(network: Network) {
                    super.onLost(network)
                    postValue(false)
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    postValue(true)
                }
            }
            return newtworkCallback
        }else{
            throw IllegalAccessError("Error")
        }
    }
    private  val networkReceiver=object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            updateConnection()
        }

    }
    private fun updateConnection(){
        val activeNetwork=connectivityManager?.activeNetworkInfo
        postValue(activeNetwork?.isConnected==true)
    }
}