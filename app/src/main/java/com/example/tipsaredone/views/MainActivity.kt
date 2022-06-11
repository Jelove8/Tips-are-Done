package com.example.tipsaredone.views

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.ActivityMainBinding
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.MyEmployees
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var showTitleScreenOnCreate: Boolean = true     // Set to FALSE after title screen is hidden, Set to TRUE onDestroy or when employees are saved to int_storage

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
        // If screen is rotated, prevents title screen from reappearing
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !showTitleScreenOnCreate) {
            showTitleScreenOnCreate = false
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && !showTitleScreenOnCreate) {
            showTitleScreenOnCreate = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        showTitleScreenOnCreate = true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun showTitleScreen(resetBool: Boolean? = null) {

        // showTitleScreen(true) is only called when user presses "SAVE" button at DistributionFragment
        if (resetBool == true) {
            binding.toolbar.visibility = View.GONE
            showTitleScreenOnCreate = true
        }

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

    fun saveEmployeeData(employees: MutableList<Employee>) {

        val employeeNames = mutableListOf<String>()
        for (emp in employees) {
            employeeNames.add(emp.name)
        }

        val jsonString = MyEmployees().convertEmployeeNamesToJson(employeeNames)
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