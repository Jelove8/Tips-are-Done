package com.example.tipsaredone.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.databinding.ActivityMainBinding
import com.example.tipsaredone.model.DatabaseModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.IndividualTipReport
import com.example.tipsaredone.model.WeeklyTipReport
import com.example.tipsaredone.viewmodels.DatePickerViewModel
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.HoursViewModel
import com.example.tipsaredone.viewmodels.TipCollectionViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    companion object {
        const val WEEKLY_REPORT = "weekly_report"
        const val EXTRA_INDIVIDUAL_TIP_REPORTS = "Individual Employee Tip Reports"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var weeklyTipReport: WeeklyTipReport

    private lateinit var databaseModel: DatabaseModel

    // ViewModels
    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var datePickerViewModel: DatePickerViewModel
    private lateinit var hoursViewModel: HoursViewModel
    private lateinit var collectionViewModel: TipCollectionViewModel

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
        datePickerViewModel = ViewModelProvider(this)[DatePickerViewModel::class.java]
        collectionViewModel = ViewModelProvider(this)[TipCollectionViewModel::class.java]
        hoursViewModel = ViewModelProvider(this)[HoursViewModel::class.java]

        binding.includeContentMain.navEmployees.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.EmployeeListFragment)
        }
        binding.includeContentMain.navTips.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.EmployeeHoursFragment)
        }
        binding.includeContentMain.navReports.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.WeeklyReportsFragment)
        }
        binding.includeContentMain.navSettings.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.SettingsFragment)
        }
    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser == null) {
            navigateToUserLoginActivity()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
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

    // Animations
    /*
    fun displayTitleAnimation() {

        ObjectAnimator.ofFloat(binding.includeContentMain.includeLogin.root, "translationY", 1000f).apply {
            duration = 500
            start()
        }
        ObjectAnimator.ofFloat(binding.includeContentMain.includeTitleScreen.root, "translationY", 400f).apply {
            duration = 500
            start()
        }
        Timer().schedule(600) {
            this@MainActivity.runOnUiThread {
                Timer().schedule(500) {
                    this@MainActivity.runOnUiThread {
                        binding.toolbar.visibility = View.VISIBLE
                    }
                }
                ObjectAnimator.ofFloat(binding.includeContentMain.includeTitleScreen.root, "translationY", -2000f).apply {
                    duration = 600
                    start()
                }

                ObjectAnimator.ofFloat(binding.includeContentMain.tvTitleScreenBackground, "translationY", -2200f).apply {
                    duration = 600
                    start()
                }
                Timer().schedule(550) {
                    this@MainActivity.runOnUiThread {
                        binding.includeContentMain.includeTitleScreen.root.visibility = View.GONE
                        binding.includeContentMain.tvTitleScreenBackground.visibility = View.GONE
                    }
                }
            }
        }
    }
*/

    fun getWeeklyTipReport(): WeeklyTipReport {
        return weeklyTipReport
    }

    // Firebase Auth
    fun navigateToUserLoginActivity() {
        val intent = Intent(this,UserLoginActivity::class.java)
        startActivity(intent)
    }

    fun navigateToReportActivity() {
        val intent = Intent(this,ReportActivity::class.java)

        val tipReportsJson = Json.encodeToJsonElement(employeesViewModel.employees.value!!)
        Log.d("meow",tipReportsJson.toString())
        intent.putExtra(EXTRA_INDIVIDUAL_TIP_REPORTS,tipReportsJson.toString())

    }


    // Firebase Database
    fun initializeEmployeesFromDatabase(employeesAdapter: EmployeesAdapter) {
        databaseModel = DatabaseModel()
        databaseModel.readEmployeeDataFromDatabase(employeesAdapter)
    }
    fun addNewEmployeeToDatabase(newEmployee: Employee) {
        databaseModel.createNewEmployee(newEmployee)
    }
    fun updateExistingEmployee(selectedEmployee: Employee) {
        employeesViewModel.employees.value!!.forEach {
            if (it.id == selectedEmployee.id) {
                it.name = selectedEmployee.name
                it.tipReports = selectedEmployee.tipReports
            }
        }
        databaseModel.updateEmployee(selectedEmployee)
    }
    fun deleteExistingEmployee(selectedEmployee: Employee) {
        databaseModel.deleteExistingEmployee(selectedEmployee)
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

    fun displayNavbar(isVisible: Boolean) {
        if (isVisible) {
            binding.includeContentMain.mainNavbar.visibility = View.VISIBLE
        }
        else {
            binding.includeContentMain.mainNavbar.visibility = View.GONE
        }
    }

    fun hideKeyboard() {
        val inputManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = this.currentFocus ?: return
        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
    }



























}