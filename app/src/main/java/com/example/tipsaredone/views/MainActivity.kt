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
import com.example.tipsaredone.model.*
import com.example.tipsaredone.viewmodels.TipCollectionViewModel
import com.example.tipsaredone.viewmodels.DatePickerViewModel
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.HoursViewModel
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // Other Components
    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var datePickerViewModel: DatePickerViewModel
    private lateinit var hoursViewModel: HoursViewModel
    private lateinit var collectionViewModel: TipCollectionViewModel

    private lateinit var weeklyTipReport: WeeklyTipReport




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


    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
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



    fun getWeeklyTipReport(): WeeklyTipReport {
        return weeklyTipReport
    }
    fun initializeWeeklyTipReport() {
        weeklyTipReport = WeeklyTipReport()
        weeklyTipReport.startDate = datePickerViewModel.startDate.value!!
        weeklyTipReport.endDate = datePickerViewModel.endDate.value!!
        weeklyTipReport.initializeIndividualReports(employeesViewModel.employees.value!!)
        hoursViewModel.initializeTipReports(weeklyTipReport.individualReports)
    }
    fun initializeWeeklyTipCollection() {
        weeklyTipReport.initializeTipsCollected(collectionViewModel.billsList.value!!)
    }
    fun logWeeklyTipReport() {
        Log.d("tip_report","Week Start: ${weeklyTipReport.startDate}")
        Log.d("tip_report","Week End: ${weeklyTipReport.endDate}\n")

        Log.d("tip_report","Tips Collected: ${weeklyTipReport.sumOfBills}")
        for ((i,type) in weeklyTipReport.bills.withIndex()) {
            when (i) {
                0 -> {
                    Log.d("tip_report","Ones: $type")
                }
                1 -> {
                    Log.d("tip_report","Twos: $type")
                }
                2 -> {
                    Log.d("tip_report","Fives: $type")
                }
                3 -> {
                    Log.d("tip_report","Tens: $type")
                }
                4 -> {
                    Log.d("tip_report","Twenties: $type")
                }
                5 -> {
                    Log.d("tip_report","Fifties: $type")
                }
                6 -> {
                    Log.d("tip_report","Hundreds: $type\n")
                }
            }
        }

        Log.d("tip_report","Tip Rate: ${weeklyTipReport.tipRate}")
        Log.d("tip_report","Error: ${weeklyTipReport.majorRoundingError}\n")

        Log.d("tip_report","Individual Tip Reports")
        weeklyTipReport.individualReports.forEach {
            Log.d("tip_report","Employee: ${it.employeeName}, Hours: ${it.employeeHours}, Distributed Tips: ${it.distributedTips}, Week Start: ${it.startDate}, Week End: ${it.endDate}, Collected: ${it.collected}")
        }
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