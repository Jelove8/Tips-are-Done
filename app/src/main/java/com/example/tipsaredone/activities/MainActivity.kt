package com.example.tipsaredone.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.adapters.HoursAdapter
import com.example.tipsaredone.adapters.WeeklyReportsAdapter
import com.example.tipsaredone.databinding.ActivityMainBinding
import com.example.tipsaredone.model.*
import com.example.tipsaredone.viewmodels.DatePickerViewModel
import com.example.tipsaredone.viewmodels.HoursViewModel
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.TipCollectionViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // ViewModels
    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var hoursViewModel: HoursViewModel
    private lateinit var datePickerViewModel: DatePickerViewModel
    private lateinit var collectionViewModel: TipCollectionViewModel

    // Init Checks
    private var employeesAndIndividualReportsInitialized = false
    private var employeeHoursInitialized = false
    private var weeklyReportsInitialized = false

    // Misc
    private var databaseModel: DatabaseModel? = null
    private lateinit var weeklyReportGenerator: WeeklyReportGenerator

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
        hoursViewModel = ViewModelProvider(this)[HoursViewModel::class.java]
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
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    // Database Model Init
    fun initializeDatabaseModel() {
        if (databaseModel == null) {
            databaseModel = DatabaseModel()
        }
    }
    fun initializeEmployeesAndIndividualReports(employeeAdapter: EmployeesAdapter) {
        employeesViewModel = ViewModelProvider(this)[EmployeesViewModel::class.java]
        if (!employeesAndIndividualReportsInitialized) {
            databaseModel!!.initializeEmployees(employeeAdapter)
            databaseModel!!.initializeIndividualReports(employeeAdapter)
            employeesAndIndividualReportsInitialized = true
        }
    }
    fun initializeEmployeeHours(hoursAdapter: HoursAdapter) {
        if (!employeeHoursInitialized) {
            databaseModel!!.initializeEmployeeHours(hoursAdapter)
            employeeHoursInitialized = true
        }
    }

    fun initializeWeeklyReports(weeklyReportsAdapter: WeeklyReportsAdapter) {
        if (!weeklyReportsInitialized) {
            databaseModel!!.initializeWeeklyReports(weeklyReportsAdapter)
            weeklyReportsInitialized = true
        }
    }

    // Employee List
    fun editExistingEmployee(editedEmployee: Employee) {
        val employeeEditedBool = databaseModel!!.editExistingEmployee(editedEmployee)
        if (employeeEditedBool) {
            hoursViewModel.employeeHours.value!!.forEach {
                if (it.id == editedEmployee.id) {
                    it.name = editedEmployee.name
                }
            }
        }
    }
    fun deleteExistingEmployee(deletedEmployee: Employee) {
        val employeeDeletedBool = databaseModel!!.deleteExistingEmployee(deletedEmployee)
        if (employeeDeletedBool) {
            hoursViewModel.employeeHours.value!!.forEach {
                if (it.id == deletedEmployee.id) {
                    hoursViewModel.employeeHours.value!!.remove(it)
                }
            }
        }
    }
    fun addNewEmployee(name: String) {
        val existingEmployees = databaseModel!!.employees
        val newEmployeeID = generateEmployeeUID(existingEmployees)

        val newEmployee = Employee(name,newEmployeeID)
        val newEmployeeAddedBool = databaseModel!!.addNewEmployee(newEmployee)

        if (newEmployeeAddedBool) {
            employeesViewModel.addEmployee(newEmployee)
            hoursViewModel.addNewEmployee(newEmployee)
        }
    }
    fun collectEmployeeTips(selectedEmployeeID: String, selectedReportID: String, isCollecting: Boolean) : Boolean {
        return databaseModel!!.collectTipsFromSpecificWeek(selectedEmployeeID,selectedReportID,isCollecting)
    }
    private fun generateEmployeeUID(existingEmployees: MutableList<Employee>): String {
        var uniqueID = UUID.randomUUID().toString()
        existingEmployees.forEach {
            if (uniqueID == it.id) {
                uniqueID = generateEmployeeUID(existingEmployees)
            }
        }
        return uniqueID
    }

    // Weekly Report
    fun getWeeklyReportGenerator(): WeeklyReportGenerator {
        return weeklyReportGenerator
    }
    fun generateNewWeeklyReport(startDate: String, endDate: String, employeeHours: MutableList<EmployeeHours>) {
        weeklyReportGenerator = WeeklyReportGenerator(startDate,endDate)
        employeeHours.forEach {
            weeklyReportGenerator.addIndividualReport(IndividualReport(it.name,it.id,"$startDate-$endDate",startDate,endDate,it.hours!!))
        }
    }
    fun collectWeeklyTips(collectedTips: MutableList<Double>) {
        weeklyReportGenerator.collectWeeklyTips(collectedTips)
        weeklyReportGenerator.distributeWeeklyTips()
    }
    fun saveNewWeeklyReport() {
        val newWeeklyReport = weeklyReportGenerator.getWeeklyReport()
        databaseModel!!.addWeeklyReport(newWeeklyReport)

        val individualReports = weeklyReportGenerator.getIndividualReports()
        individualReports.forEach { individualReport ->
            databaseModel!!.addIndividualReport(individualReport)
        }
    }

    // Activity Navigation
    fun navigateToUserLoginActivity() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,UserLoginActivity::class.java)
        startActivity(intent)
    }

    // Misc
    fun displayNavbar(isVisible: Boolean) {
        if (isVisible) {
            binding.includeContentMain.mainNavbar.visibility = View.VISIBLE
        }
        else {
            binding.includeContentMain.mainNavbar.visibility = View.GONE
        }
    }
    fun displayCalculatingScreen() {
        binding.includeContentMain.includeCalculatingScreen.root.visibility = View.VISIBLE
        binding.toolbar.visibility = View.GONE
        Timer().schedule(1000){
            this@MainActivity.runOnUiThread {
                binding.includeContentMain.includeCalculatingScreen.root.visibility = View.GONE
                binding.toolbar.visibility = View.VISIBLE
            }
        }
    }
    fun hideKeyboard() {
        val inputManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = this.currentFocus ?: return
        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
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