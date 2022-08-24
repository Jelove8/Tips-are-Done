package com.example.tipsaredone.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.google.gson.Gson
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {
        const val WEEKLY_REPORT = "weekly_report"
        const val EXTRA_INDIVIDUAL_TIP_REPORTS = "Individual Employee Tip Reports"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var weeklyTipReport: WeeklyTipReport? = null

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


    fun getWeeklyReport(): WeeklyTipReport {
        return weeklyTipReport!!
    }
    fun createWeeklyReport() {
        val startDate = datePickerViewModel.startDate.value!!.toString()
        val endDate = datePickerViewModel.endDate.value!!.toString()
        val individualReports = employeesViewModel.individualTipReports.value!!
        weeklyTipReport = WeeklyTipReport(individualReports, startDate, endDate)
    }
    fun calculateWeeklyReport() {
        val collectedTips = collectionViewModel.tipsCollected.value!!
        val collectedTipsAsMap = mutableListOf<Map<String,Int>>()
        for ((i,item) in collectedTips.withIndex()) {
            when (i) {
                0 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Ones",item.toInt())))
                }
                1 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Twos",item.toInt())))
                }
                2 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Fives",item.toInt())))
                }
                3 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Tens",item.toInt())))
                }
                4 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Twenties",item.toInt())))
                }
                5 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Fifties",item.toInt())))
                }
                6 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Hundreds",item.toInt())))
                }
            }
        }

        val individualReports = employeesViewModel.individualTipReports.value!!
        individualReports.forEach {
            if (it.employeeHours != null) {
                weeklyTipReport!!.totalHours += it.employeeHours!!
            }
        }
        weeklyTipReport!!.totalCollected = collectedTips.sum()
        weeklyTipReport!!.distributeTips()
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