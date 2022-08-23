package com.example.tipsaredone.views

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tipsaredone.R
import com.example.tipsaredone.activities.ReportActivity
import com.example.tipsaredone.databinding.FragmentDatePickerBinding
import com.example.tipsaredone.viewmodels.DatePickerViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class DatePickerFragment : Fragment() {

    private lateinit var datePickerViewModel: DatePickerViewModel
    private lateinit var reportActivity: ReportActivity
    private var _binding: FragmentDatePickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDatePickerBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val datePickerVM: DatePickerViewModel by activityViewModels()
        datePickerViewModel = datePickerVM
        reportActivity = (context as ReportActivity)




    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkDateValidity(): Boolean {
        return if (datePickerViewModel.startDate.value != null && datePickerViewModel.endDate.value != null) {
            if (datePickerViewModel.startDate.value!!.isBefore(datePickerViewModel.endDate.value!!)) {
                true
            } else {
                resources.getString(R.string.invalid_dates2)
              // (context as MainActivity).makeToastMessage(toast)
                false
            }
        } else {
            resources.getString(R.string.invalid_dates1)
           //  (context as MainActivity).makeToastMessage(toast)
            false
        }
    }

    private fun convertDatesToString() {
        val startDate = datePickerViewModel.startDate.value!!
        val startMonthString =
            if (startDate.monthValue < 10) {
                "0${startDate.monthValue}"
            } else {
                startDate.monthValue
            }
        val startDayString =
            if (startDate.dayOfMonth < 10) {
                "0${startDate.dayOfMonth}"
            } else {
                startDate.dayOfMonth
            }
        startDate.monthValue
        val startDateString = "${startDate.year}$startMonthString$startDayString"



    }

    private fun displayStoredDatesFromVM() {
        val startDate = datePickerViewModel.startDate.value
        val endDate = datePickerViewModel.endDate.value

        if (startDate == null) {
            binding.startDatePicker.updateDate(LocalDate.now().year,LocalDate.now().monthValue,LocalDate.now().dayOfMonth)
        }
        else {
            val startYear = datePickerViewModel.startDate.value!!.year
            val startMonth = datePickerViewModel.startDate.value!!.monthValue
            val startDay = datePickerViewModel.startDate.value!!.dayOfMonth
            binding.startDatePicker.updateDate(startYear,startMonth,startDay)
        }

        if (endDate == null) {
            binding.endDatePicker.updateDate(LocalDate.now().year,LocalDate.now().monthValue,LocalDate.now().dayOfMonth)
        }
        else {
            val endYear = datePickerViewModel.endDate.value!!.year
            val endMonth = datePickerViewModel.endDate.value!!.monthValue
            val endDay = datePickerViewModel.endDate.value!!.dayOfMonth
            binding.endDatePicker.updateDate(endYear,endMonth,endDay)
        }
    }
}
