package com.example.tipsaredone.views

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
import com.example.tipsaredone.model.WeeklyTipReport
import com.example.tipsaredone.viewmodels.DatePickerViewModel
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.HoursViewModel
import com.example.tipsaredone.viewmodels.TipCollectionViewModel
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    companion object {
        const val WEEKLY_REPORT = "weekly_report"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var weeklyTipReport: WeeklyTipReport

    private var myEmployees = MyEmployees()

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
        weeklyTipReport.startDate = LocalDateTime.of(datePickerViewModel.startDate.value!!.year,datePickerViewModel.startDate.value!!.month,datePickerViewModel.startDate.value!!.dayOfMonth,0,0)
        weeklyTipReport.endDate = LocalDateTime.of(datePickerViewModel.endDate.value!!.year,datePickerViewModel.endDate.value!!.month,datePickerViewModel.endDate.value!!.dayOfMonth,0,0)
        weeklyTipReport.initializeIndividualReports(employeesViewModel.employees.value!!)
        hoursViewModel.initializeTipReports(weeklyTipReport.individualReports)

        Log.d(WEEKLY_REPORT,"Initialized")
        Log.d(WEEKLY_REPORT,"Start: ${weeklyTipReport.startDate},  End: ${weeklyTipReport.endDate}")
        var employeeNames = ""
        for ((i,report) in weeklyTipReport.individualReports.withIndex()) {
            employeeNames += if (i == 0) {
                report.employeeName
            } else {
                ", ${report.employeeName}"
            }
        }
        Log.d(WEEKLY_REPORT,employeeNames)
    }

    fun initializeMyEmployees() {

    }

    fun logWeeklyTipReport() {
        Log.d("tip_report","Week Start: ${weeklyTipReport.startDate}")
        Log.d("tip_report","Week End: ${weeklyTipReport.endDate}")

        Log.d("tip_report","Tips Collected: ${weeklyTipReport.sumOfBills}")
        for ((i,type) in weeklyTipReport.bills.withIndex()) {
            when (i) {
                0 -> {
                    Log.d("tip_report"," $type")
                }
                1 -> {
                    Log.d("tip_report"," $type")
                }
                2 -> {
                    Log.d("tip_report"," $type")
                }
                3 -> {
                    Log.d("tip_report"," $type")
                }
                4 -> {
                    Log.d("tip_report"," $type")
                }
                5 -> {
                    Log.d("tip_report"," $type")
                }
                6 -> {
                    Log.d("tip_report"," $type")
                }
            }
        }

        Log.d("tip_report","Tip Rate: ${weeklyTipReport.tipRate}")
        Log.d("tip_report","Error: ${weeklyTipReport.majorRoundingError}")

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