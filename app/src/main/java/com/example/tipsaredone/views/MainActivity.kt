package com.example.tipsaredone.views

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.ActivityMainBinding
import com.example.tipsaredone.model.MyEmployees
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {

    companion object {
        const val MISC: String = "misc"
    }

    init {
        Log.d("Initial","MainActivity initialized.")
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // Other Components
    private lateinit var employeesViewModel: EmployeesViewModel

    // Title Screen Configurations
    private var visibleTitleScreen: Boolean = true
    private var visibleToolBar: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuring Navigation and ActionBar components.
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Displays title screen if configurations are set to default.
        showTitleScreen()

        // Initialize viewmodels
        employeesViewModel = ViewModelProvider(this)[EmployeesViewModel::class.java]

        
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !visibleTitleScreen) {
            // Title Screen Configurations set to HIDDEN
            visibleTitleScreen = false
            visibleToolBar = true
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && !visibleTitleScreen) {
            // Title Screen Configurations set to HIDDEN
            visibleTitleScreen = false
            visibleToolBar = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // If MainActivity is destroyed, resets title screen configurations.
        visibleTitleScreen = true
        visibleToolBar = false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun showTitleScreen(reset: Boolean? = null) {

        if (reset == true) {    // Resets title screen configurations to default (Only called when user presses "SAVE" button @DistributionFragment)
            visibleTitleScreen = true
            binding.includeTitleScreen.root.visibility = View.VISIBLE
            visibleToolBar = false
            binding.toolbar.visibility = View.GONE
        }

        if (visibleTitleScreen && !visibleToolBar) {    // if (default title screen configurations)
            // Makes title screen view visible.
            binding.includeTitleScreen.root.visibility = View.VISIBLE

            // Hiding title screen & showing toolbar after a delay.
            Timer().schedule(2000){
                this@MainActivity.runOnUiThread {
                    visibleTitleScreen = false
                    binding.includeTitleScreen.root.visibility = View.GONE
                    visibleToolBar = true
                    binding.toolbar.visibility = View.VISIBLE
                }
            }
        }
    }



    // Toolbar Visibility (used @DistributionFragment)
    fun hideToolbar() {
        binding.toolbar.visibility = View.GONE
        visibleToolBar = false
    }
    fun showToolbar() {
        binding.toolbar.visibility = View.VISIBLE
        visibleToolBar = true
    }

    // Initialize Employees in ViewModel

    // Misc
    fun makeToastMessage(message: String, isDurationShort: Boolean = true) {
        if (isDurationShort) {
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        }
    }
}