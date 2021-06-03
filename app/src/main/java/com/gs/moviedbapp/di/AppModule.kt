package com.gs.moviedbapp.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.room.Room
import com.gs.moviedbapp.data.local.FavouriteMovieDB
import com.gs.moviedbapp.data.network.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

@Singleton
@Provides
    fun setRoomDb(@ApplicationContext app:Context)= Room.databaseBuilder(
        app,
        FavouriteMovieDB::class.java,
        "movie_db"
        ).build()



    @Singleton
    @Provides
    fun getRoomDB(db:FavouriteMovieDB)=db.getFavouriteMovieDao()



    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    fun hasNetwork(@ApplicationContext context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor,@ApplicationContext context: Context): OkHttpClient {
        /*val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)*/
        val cacheSize = 10 * 1024 * 1024 // 10 MB

        val mycache = Cache(context.cacheDir, cacheSize.toLong())
        /*val okHttpClient =*/

        return  OkHttpClient().newBuilder()

            .callTimeout(40, TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .cache(mycache)
            //.addInterceptor(loggingInterceptor)
            .addInterceptor {
                    chain ->

                var request = chain.request()
                request = if (hasNetwork(context)!!)
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                else
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()
        /*return okHttpClient.build()*/
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit=
        Retrofit.Builder()
                .client(okHttpClient)
            .baseUrl(MovieApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit):MovieApi=
        retrofit.create(MovieApi::class.java)



}