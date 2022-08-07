package com.example.tipsaredone.views

import android.animation.ObjectAnimator
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
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.databinding.ActivityMainBinding
import com.example.tipsaredone.model.*
import com.example.tipsaredone.viewmodels.DatePickerViewModel
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.HoursViewModel
import com.example.tipsaredone.viewmodels.TipCollectionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    companion object {
        const val WEEKLY_REPORT = "weekly_report"
        const val FIREBASE = "firebase"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private var signInRequired: Boolean = true

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

        // Firebase
        databaseModel = DatabaseModel()

        // Initialize ViewModels
        employeesViewModel = ViewModelProvider(this)[EmployeesViewModel::class.java]
        datePickerViewModel = ViewModelProvider(this)[DatePickerViewModel::class.java]
        collectionViewModel = ViewModelProvider(this)[TipCollectionViewModel::class.java]
        hoursViewModel = ViewModelProvider(this)[HoursViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun showTitleScreen() {
        binding.includeTitleScreen.root.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(binding.includeTitleScreen.root, "translationY", 100f).apply {
            duration = 2000
            start()
        }
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
        weeklyTipReport.startDate = datePickerViewModel.getStartDateString()
        weeklyTipReport.endDate = datePickerViewModel.getEndDateString()
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

    /*
    fun saveWeeklyTipReport() {
        val weeklyTipReportForStorage = mapOf(
            "${weeklyTipReport.startDate.toString().removeSuffix("T00:00")} to ${weeklyTipReport.endDate.toString().removeSuffix("T00:00")}" to
            weeklyTipReport)

        firebaseDB.collection("WeeklyTipReports").document("AllReports").update(weeklyTipReportForStorage)
            .addOnSuccessListener {
                Log.d(WEEKLY_REPORT,"New weekly tip report added to database.")
            }
            .addOnFailureListener {
                Log.d(WEEKLY_REPORT,"Failed to add weekly tip report to database.", it)
            }
    }
     */


    // Firebase Auth
    fun getSignInBool(): Boolean {
        return signInRequired
    }
    fun signInUser(email: String, password: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signInRequired = false
                    findNavController(R.id.nav_host_fragment).navigate(R.id.action_userLoginFragment_to_EmployeeListFragment)
                    binding.toolbar.visibility = View.VISIBLE
                } else {
                    val toast = resources.getString(R.string.login_failed)
                    makeToastMessage(toast)
                }
            }
    }
    fun signOutUser() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        signInRequired = true
    }
    fun signUpNewUser(email: String, password: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signInUser(email,password)
                } else {
                    val toast = resources.getString(R.string.sign_up_failed)
                    makeToastMessage(toast)
                }
            }.addOnFailureListener {
                makeToastMessage(it.toString())
            }
    }

    // Firestore
    fun initializeDatabaseModel(adapter: EmployeesAdapter){
        val employeesFromDatabase = databaseModel.initializeEmployees(firebaseAuth)
        employeesViewModel.initializeEmployees(employeesFromDatabase)
    }
    fun addNewEmployeeToDatabase(newEmployee: Employee) {
        databaseModel.addNewEmployee(newEmployee)
    }
    fun saveEmployeesToDatabase() {
    }
    fun getDatabaseModel(): DatabaseModel {
        return databaseModel
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