package com.gs.moviedbapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       /* val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)*/


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.movieHome, R.id.savedMovie
        ).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        /*binding.apply {*/
            bottomNavigationView.setupWithNavController(navController)
      /*  }*/

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

  /*  private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(applicationContext, "press again to exit..", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }*/

}