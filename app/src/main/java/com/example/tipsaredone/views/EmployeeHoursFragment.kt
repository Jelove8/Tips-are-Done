package com.example.tipsaredone.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.activities.MainActivity
import com.example.tipsaredone.activities.ReportActivity
import com.example.tipsaredone.adapters.HoursAdapter
import com.example.tipsaredone.databinding.FragmentEmployeeHoursBinding
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.HoursViewModel

class EmployeeHoursFragment : Fragment() {

    private lateinit var hoursViewModel: HoursViewModel
    private lateinit var hoursAdapter: HoursAdapter

    private var _binding: FragmentEmployeeHoursBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmployeeHoursBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize EmployeesViewModel
        val hoursVM: HoursViewModel by activityViewModels()
        hoursViewModel = hoursVM

        val employeesVM: EmployeesViewModel by activityViewModels()
        hoursViewModel.initializeTipReports(employeesVM.employees.value!!)

        hoursAdapter = HoursAdapter(hoursViewModel.employeeHoursList.value!!,
            // TextChanged: When employee hours are edited.
            textChangedCallback = fun(_: Int) {
                updateSumOfHours()
                updateConfirmButtonVisibility()
            }
        )
        binding.rcyEmployeeHours.layoutManager = LinearLayoutManager(context as ReportActivity)
        binding.rcyEmployeeHours.adapter = hoursAdapter
        hoursAdapter.initializeTipReports(employeesVM.employees.value!!)

        updateConfirmButtonVisibility()
        updateSumOfHours()
        // Button: Confirm employees and navi
        // gate to next fragment.
        binding.btnEmployeeHoursConfirm.setOnClickListener {
            if (hoursAdapter.checkForValidHours()) {
                findNavController().navigate(R.id.action_HoursFrag_to_CollectionFrag)
            }
            else {
                val toast = resources.getString(R.string.employee_hours_required)
                (context as MainActivity).makeToastMessage(toast)
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    // Updates Views
    private fun updateConfirmButtonVisibility() {
        if (hoursAdapter.checkForValidHours()) {
            val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as ReportActivity).theme)
            binding.btnEmployeeHoursConfirm.setBackgroundColor(sbGreen)
        }
        else {
            val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as ReportActivity).theme)
            binding.btnEmployeeHoursConfirm.setBackgroundColor(wrmNeutral)
        }
    }
    private fun updateSumOfHours() {
        binding.tvTotalHours.text = hoursAdapter.getSumOfHours().toString()
    }


    private fun displayDatePicker(isShown: Boolean) {
        if (isShown) {
            binding.subcnstDatePicker.visibility = View.VISIBLE
        }
        else {
            binding.subcnstDatePicker.visibility = View.GONE
        }
    }







































}