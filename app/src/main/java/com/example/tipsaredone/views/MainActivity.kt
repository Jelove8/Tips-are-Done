package com.example.tipsaredone.views

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.ActivityMainBinding
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var showTitleScreenOnCreate: Boolean = true     // Set to FALSE after title screen is hidden, Set to TRUE only onDestroy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Shows title screen once
        showTitleScreen()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // If screen is rotated, prevents title screen from reappearing
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showTitleScreenOnCreate = false
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            showTitleScreenOnCreate = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        showTitleScreenOnCreate = true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun makeToastMessage(message: String, isDurationShort: Boolean = true) {
        if (isDurationShort) {
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        }
    }

    private fun showTitleScreen() {
        if (showTitleScreenOnCreate) {
            binding.includeTitleScreen.root.visibility = View.VISIBLE

            Timer().schedule(2000){
                this@MainActivity.runOnUiThread {
                    // Hiding title screen & showing toolbar
                    binding.includeTitleScreen.root.visibility = View.GONE
                    binding.toolbar.visibility = View.VISIBLE
                    showTitleScreenOnCreate = false
                }
            }

        }
    }

    // Used by "calculating tips" loading screen in DistributionFragment
    fun hideToolbar() {
        binding.toolbar.visibility = View.GONE
    }
    fun showToolbar() {
        binding.toolbar.visibility = View.VISIBLE
    }
}