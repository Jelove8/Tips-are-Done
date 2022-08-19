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
import com.example.tipsaredone.model.EmployeeHours
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.HoursViewModel

class EmployeeHoursFragment : Fragment() {

    private lateinit var hoursViewModel: HoursViewModel
    private lateinit var hoursAdapter: HoursAdapter

    private lateinit var employeesViewModel: EmployeesViewModel
    private var initBool: Boolean = true

    private var _binding: FragmentEmployeeHoursBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmployeeHoursBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Initialize EmployeesViewModel
        val hoursVM: HoursViewModel by activityViewModels()
        hoursViewModel = hoursVM

        val employeesVM: EmployeesViewModel by activityViewModels()
        employeesViewModel = employeesVM


        hoursAdapter = HoursAdapter(employeesViewModel.individualTipReports.value!!,
            // TextChanged: When employee hours are edited.
            textChangedCallback = fun(_: Int) {

            }
        )
        binding.rcyEmployeeHours.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyEmployeeHours.adapter = hoursAdapter




        // Button: Confirm employees and navi
        // gate to next fragment.


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    // Updates Views










































}