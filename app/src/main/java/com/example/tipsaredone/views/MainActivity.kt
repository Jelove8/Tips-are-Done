package com.example.tipsaredone.views

import android.content.res.Configuration
import android.os.Bundle
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
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.MyEmployees
import com.example.tipsaredone.model.RoughTipReport
import com.example.tipsaredone.viewmodels.CollectionViewModel
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // Other Components
    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var collectionViewModel: CollectionViewModel
    private val roughTipReport = RoughTipReport()
    private val myEmployees = MyEmployees()

    // Title Screen Configurations
    private var visibleTitleScreen: Boolean = true
    private var visibleToolBar: Boolean = false

    // Internal Storage Boolean


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuring Navigation and ActionBar components.
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        // Initialize ViewModels
        employeesViewModel = ViewModelProvider(this)[EmployeesViewModel::class.java]
        collectionViewModel = ViewModelProvider(this)[CollectionViewModel::class.java]


        // Displays title screen if configurations are set to default.
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

    fun getEmployeesFromStorage(): MutableList<Employee> {
        myEmployees.loadEmployeeNamesFromInternalStorage(this)
        return myEmployees.getStoredEmployees()
    }
    fun saveEmployeesToStorage() {
        MyEmployees().saveEmployeeNamesToInternalStorage(employeesViewModel.employees.value!!,this)
    }

    fun showTitleScreen() {
        binding.includeTitleScreen.root.visibility = View.VISIBLE
        binding.toolbar.visibility = View.GONE
        Timer().schedule(2000){
            this@MainActivity.runOnUiThread {
                binding.includeTitleScreen.root.visibility = View.GONE
                binding.toolbar.visibility = View.VISIBLE
            }
        }
    }
    fun showCalculatingScreen() {
        binding.includeCalculatingScreen.root.visibility = View.VISIBLE
        binding.toolbar.visibility = View.GONE
        Timer().schedule(1200){
            this@MainActivity.runOnUiThread {
                binding.includeCalculatingScreen.root.visibility = View.GONE
                binding.toolbar.visibility = View.VISIBLE
            }
        }
    }

    // Toolbar Visibility (used @DistributionFragment)
    private fun hideToolbar() {
        binding.toolbar.visibility = View.GONE
        visibleToolBar = false
    }
    private fun showToolbar() {
        binding.toolbar.visibility = View.VISIBLE
        visibleToolBar = true
    }

    // Tip Report
    fun getRoughTipReport(): RoughTipReport {
        return roughTipReport
    }

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