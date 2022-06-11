package com.example.tipsaredone.views

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
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
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.MyEmployees
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // Title Screen Configurations
    private var visibleTitleScreen: Boolean = true
    private var visibleToolBar: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Shows title screen once
        showTitleScreen()
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

    fun showTitleScreen(reset: Boolean? = null) {   // showTitleScreen(true) is only called when user presses "SAVE" button at DistributionFragment

        if (reset == true) {    // Resets title screen configurations
            visibleTitleScreen = true
            binding.includeTitleScreen.root.visibility = View.VISIBLE
            visibleToolBar = false
            binding.toolbar.visibility = View.GONE
        }

        if (visibleTitleScreen && !visibleToolBar) {
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

    // Reading & Writing employee names from & to internal storage.
    fun saveEmployeeNamesToInternalStorage(employees: MutableList<Employee>) {        // Called whenever the "CONTINUE" button is pressed @EmployeeListFragment.
        // Converting list of employee objects into list of employee names (string).
        val listOfNames = mutableListOf<String>()
        for (emp in employees) {
            listOfNames.add(emp.name)
        }

        // Converting listOfNames to Json
        val jsonString = MyEmployees().convertEmployeeNamesToJson(listOfNames)
        val jsonFileName = "myEmployees"

        // Saving the Json data to internal storage
        this.openFileOutput(jsonFileName, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
            it.close()
            Log.d(MyEmployees.INTERNAL_STORAGE, "Employee names saved as Json:\n${jsonString}")
        }

    }



    // Used by "calculating tips" loading screen in DistributionFragment
    fun hideToolbar() {
        binding.toolbar.visibility = View.GONE
    }
    fun showToolbar() {
        binding.toolbar.visibility = View.VISIBLE
    }

    fun makeToastMessage(message: String, isDurationShort: Boolean = true) {
        if (isDurationShort) {
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        }
    }
}