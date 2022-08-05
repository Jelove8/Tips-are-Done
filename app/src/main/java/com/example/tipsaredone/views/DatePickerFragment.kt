package com.example.tipsaredone.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.FragmentDatePickerBinding
import com.example.tipsaredone.viewmodels.DatePickerViewModel
import java.time.LocalDate
import java.time.ZoneOffset

class DatePickerFragment : Fragment() {

    companion object {
        const val SEVEN_DAYS_IN_MILLISECONDS_LONG = 604800000.toLong()
        const val MILLISECONDS_IN_ONE_DAY = 86400000
    }

    private lateinit var datePickerViewModel: DatePickerViewModel
    private var dateInputsInitialized = false

    private var _binding: FragmentDatePickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDatePickerBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val datePickerVM: DatePickerViewModel by activityViewModels()
        datePickerViewModel = datePickerVM

        if (datePickerViewModel.startDate.value == null || datePickerViewModel.endDate.value == null) {
            binding.inputStartDate.updateDate(LocalDate.now().year,LocalDate.now().monthValue,LocalDate.now().dayOfMonth)
            binding.inputEndDate.updateDate(LocalDate.now().year,LocalDate.now().monthValue,LocalDate.now().dayOfMonth)
        }
        else {
            val startDate = datePickerViewModel.startDate.value!!
            val endDate = datePickerViewModel.endDate.value!!
            binding.inputStartDate.updateDate(startDate.year,startDate.monthValue,startDate.dayOfMonth)
            binding.inputEndDate.updateDate(endDate.year,endDate.monthValue,endDate.dayOfMonth)
        }

        binding.inputStartDate.setOnDateChangedListener { _, year, month, dayOfMonth ->
            Log.d("meow","hello")
            datePickerViewModel.setStartDate(year,month,dayOfMonth)
        }
        binding.inputEndDate.setOnDateChangedListener { _, year, month, dayOfMonth ->
            Log.d("meow","hello")
            datePickerViewModel.setEndDate(year,month,dayOfMonth)
        }
        binding.btnDatePickerConfirm.setOnClickListener {
            if (checkDateValidity()) {
                Log.d("meow","hello")
                (context as MainActivity).initializeWeeklyTipReport()
                findNavController().navigate(R.id.action_DatePickerFrag_to_EmployeeHoursFrag)
            }
        }
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
                val toast = resources.getString(R.string.invalid_dates2)
                (context as MainActivity).makeToastMessage(toast)
                false
            }
        } else {
            val toast = resources.getString(R.string.invalid_dates1)
            (context as MainActivity).makeToastMessage(toast)
            false
        }
    }
}