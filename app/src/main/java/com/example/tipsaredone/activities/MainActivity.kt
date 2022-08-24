package com.example.tipsaredone.activities

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
import com.example.tipsaredone.model.*
import com.example.tipsaredone.viewmodels.DatePickerViewModel
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.TipCollectionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import java.time.LocalDate
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {
        const val WEEKLY_REPORT = "weekly_report"
        const val EXTRA_INDIVIDUAL_TIP_REPORTS = "Individual Employee Tip Reports"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var newWeeklyReport: WeeklyReport? = null

    private lateinit var databaseModel: DatabaseModel

    // ViewModels
    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var datePickerViewModel: DatePickerViewModel
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

        // Navbar Buttons
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

    // Weekly Report
    fun getWeeklyReport(): WeeklyReport {
        return newWeeklyReport!!
    }
    fun createWeeklyReport(startDate: LocalDate, endDate: LocalDate) {
        newWeeklyReport = WeeklyReport(startDate, endDate)
    }

    // Activity Navigation
    fun navigateToUserLoginActivity() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,UserLoginActivity::class.java)
        startActivity(intent)
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

    fun addNewWeeklyReportToDatabase(newWeeklyReport: WeeklyReport) {
        databaseModel.saveWeeklyReport(newWeeklyReport)
    }

    // JSON
    fun convertEmployeesToJson(employees: MutableList<Employee>): ArrayList<String> {
        val gson = Gson()
        val employeesJsonList = arrayListOf<String>()
        employees.forEach {
            employeesJsonList.add(gson.toJson(it))
        }
        return employeesJsonList
    }
    fun convertIndividualReportsToJson(individualReports: MutableList<IndividualTipReport>): ArrayList<String> {
        val gson = Gson()
        val reportsJson = arrayListOf<String>()
        individualReports.forEach {
            reportsJson.add(gson.toJson(it))
        }
        return reportsJson
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