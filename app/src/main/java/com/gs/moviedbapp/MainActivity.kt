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

companion object{
    private  val MY_PREFERENCES="mypreferences"
    private  val KEY_ISNIGHTMODE="isnightmode"

}

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences=getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.movieHome, R.id.savedMovie
        ).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)


    }




    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val item = menu!!.findItem(R.id.switchId)
        item!!.setActionView(R.layout.switch_layout)

        val mySwitch = item.actionView.findViewById<SwitchCompat>(R.id.switchAB)
        checkNightMode(item)
        mySwitch.setOnCheckedChangeListener { p0, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveNightModestate(true)


            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                saveNightModestate(false)


            }
            recreate()
        }

        return true
    }

    private fun saveNightModestate(b: Boolean) {
           val editor:SharedPreferences.Editor=sharedPreferences.edit()
               editor.putBoolean(KEY_ISNIGHTMODE,b)
        editor.apply()
    }
    private fun checkNightMode( item: MenuItem) {
        val mswitch=item.actionView.findViewById<SwitchCompat>(R.id.switchAB)
        if(sharedPreferences.getBoolean(KEY_ISNIGHTMODE,false)){
            mswitch.isChecked=true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            mswitch.isChecked=false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO )
        }
    }


}