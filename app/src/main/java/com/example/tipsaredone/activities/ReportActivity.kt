
package com.example.tipsaredone.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.ActivityReportBinding
import com.example.tipsaredone.model.WeeklyTipReport
import com.google.gson.Gson


class ReportActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityReportBinding
    // 0 = Employee Hours, 1 = Date Picker, 2  = Tip Collection

    private lateinit var weeklyTipReport: WeeklyTipReport

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        val navController = findNavController(R.id.nav_host_fragment_content_report)
        setupActionBarWithNavController(navController, appBarConfiguration)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        initializeWeeklyTipReport()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    Log.d("meow","Home")
                }
            }
        this.onBackPressedDispatcher.addCallback(this, callback)

        binding.btnFab.setOnClickListener {

        }
    }


    private fun initializeWeeklyTipReport(){
        val gson = Gson()
        val weeklyTipReportJson = intent.getStringExtra("new_weekly_tip_report")
        val newWeeklyTipReport = gson.fromJson(weeklyTipReportJson, WeeklyTipReport::class.java)
        weeklyTipReport = newWeeklyTipReport
        Log.d("meow","$weeklyTipReport")
    }

    // FAB Button
    private fun updateConfirmButtonVisibility(isVisible: Boolean) {
        if (isVisible) {
            val sbGreen = ResourcesCompat.getColor(resources, com.example.tipsaredone.R.color.starbucks_green, theme)
            binding.btnFab.setBackgroundColor(sbGreen)
        }
        else {
            val wrmNeutral = ResourcesCompat.getColor(resources, com.example.tipsaredone.R.color.warm_neutral, theme)
            binding.btnFab.setBackgroundColor(wrmNeutral)
        }
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