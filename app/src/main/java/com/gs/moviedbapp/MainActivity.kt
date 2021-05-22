package com.gs.moviedbapp


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
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
lateinit var sharedPreferences: SharedPreferences
 //var mySwitch: SwitchCompat
companion object{
    private  val MY_PREFERENCES="mypreferences"
    private  val KEY_ISNIGHTMODE="isnightmode"

}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)*/
        sharedPreferences=getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE)
         //checkNightMode(mySwitch, item)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.movieHome, R.id.savedMovie
        ).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        /*binding.apply {*/
        bottomNavigationView.setupWithNavController(navController)
        /*  }*/

    }

    private fun checkNightMode( item: MenuItem) {
        var mswitch=item.actionView.findViewById<SwitchCompat>(R.id.switchAB)
         if(sharedPreferences.getBoolean(KEY_ISNIGHTMODE,false)){
             mswitch.isChecked=true
             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
         }else{
             mswitch.isChecked=false
             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO )
         }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }




    /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu., menu)
        switchAB = menu.findItem(R.id.switchId)
            .getActionView().findViewById(R.id.switchAB) as Switch
        switchAB.setOnCheckedChangeListener(object : OnCheckedChangeListener() {
            fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                if (isChecked) {
                    Toast.makeText(application, "ON", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(application, "OFF", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        return true
    }*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val item = menu!!.findItem(R.id.switchId)
        item.setActionView(R.layout.switch_layout)

        val mySwitch = item.actionView.findViewById<SwitchCompat>(R.id.switchAB)
        checkNightMode(item)
        mySwitch.setOnCheckedChangeListener { p0, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveNightModestate(true)
                recreate()


            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                saveNightModestate(false)
                recreate()

            }
        }

        return true
    }

    private fun saveNightModestate(b: Boolean) {
           val editor:SharedPreferences.Editor=sharedPreferences.edit()
               editor.putBoolean(KEY_ISNIGHTMODE,b)
        editor.apply()
    }

/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)

        switchAB = menu.findItem(R.id.switchId)
            .actionView.findViewById<View>(R.id.switchAB) as Switch
        switchAB.setOnCheckedChangeListener(object : OnCheckedChangeListener() {
            fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                if (isChecked) {
                    Toast.makeText(application, "ON", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(application, "OFF", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        return true
    }*/


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