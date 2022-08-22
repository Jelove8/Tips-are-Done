
package com.example.tipsaredone.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.beust.klaxon.json
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.ActivityReportBinding
import com.example.tipsaredone.model.Employee
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class ReportActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding2: ActivityReportBinding
    private var fabConfiguration = 0
    // 0 = Employee Hours, 1 = Date Picker, 2  = Tip Collection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding2 = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding2.root)

        setSupportActionBar(binding2.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navController = findNavController(R.id.nav_host_fragment_content_report)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)




        binding2.btnFab.setOnClickListener {

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_report)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun getEmployeesFromIntentExtra(): MutableList<Employee> {
        val employeesJson: ArrayList<String> = intent.getStringArrayListExtra("employees")!!
        val employees = mutableListOf<Employee>()
        val gson = Gson()
        employeesJson.forEach {
            val employee: Employee = gson.fromJson(it, Employee::class.java)
            employees.add(employee)
        }
        return employees
    }

    fun displayFAB(isShowing: Boolean) {
        if (isShowing) {
            binding2.btnFab.visibility = View.VISIBLE
        }
        else {
            binding2.btnFab.visibility = View.GONE
        }
    }
    fun updateFAB(config: Int) {
        fabConfiguration = config
    }

    fun makeToastMessage(message: String, isDurationShort: Boolean = true) {
        if (isDurationShort) {
            Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
        }
    }
}