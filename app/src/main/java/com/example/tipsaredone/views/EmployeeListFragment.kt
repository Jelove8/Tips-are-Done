package com.example.tipsaredone.views

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.databinding.FragmentEmployeesListBinding
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.HoursViewModel

class EmployeeListFragment : Fragment() {

    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var employeeListAdapter: EmployeesAdapter

    private var _binding: FragmentEmployeesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmployeesListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize EmployeesViewModel
        val employeesVM: EmployeesViewModel by activityViewModels()
        employeesViewModel = employeesVM

        // Load saved data.


        employeeListAdapter = EmployeesAdapter( employeesViewModel.employees.value!!,
            /**
             * ITEMCLICK:  Navigate to EmployeeDialogFragment to edit an employee.
             */
            itemClickCallback = fun(position: Int) {
                navigateEditEmployee(position)
            }
        )
        binding.rcyEmployees.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyEmployees.adapter = employeeListAdapter

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_frag_employees, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    /**
                     * BUTTON:  Navigate to EmployeeDialogFragment to add a new employee.
                     */
                    R.id.action_add_employee -> {
                        if (binding.includeEmployeeDialog.root.visibility == View.GONE) {
                            navigateNewEmployee()
                            true
                        }
                        else {
                            false
                        }
                    }
                    else ->  false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        /**
         * BUTTON:      Navigate to EmployeeHoursFragment.
         * VISIBILITY:  Only visible/clickable if there are at least 2 existing employees.
         */
        binding.btnConfirmEmployees.setOnClickListener {

            if (!employeesViewModel.checkForValidInputs()) {
                val toast = resources.getString(R.string.employees_list_invalid_string)
                (context as MainActivity).makeToastMessage(toast)
            }
            else {
                displayDateSelector()
            }
        }
        updateConfirmButtonVisibility()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Internal Storage
    /*
    private fun loadEmployeesFromInternalStorage() {
        val data = (context as MainActivity).getEmployeesFromStorage()
        employeesViewModel.loadDataFromInternalStorage(data)
    }

     */

    // Employee Dialog
    private fun navigateEditEmployee(index: Int) {
        employeesViewModel.selectEmployee(index)
        findNavController().navigate(R.id.action_ListFragment_to_employeeDialogFragment)
    }
    private fun navigateNewEmployee() {
        employeesViewModel.selectEmployee(null)
        findNavController().navigate(R.id.action_ListFragment_to_employeeDialogFragment)
    }

    // Date Selector Dialog
    private fun displayDateSelector() {
        binding.btnConfirmEmployees.visibility = View.GONE
        binding.includeDateSelector.root.visibility = View.VISIBLE
        binding.includeDateSelector.inputStartDate2.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            employeesViewModel.setStartDate(year,monthOfYear,dayOfMonth)
        }
        binding.includeDateSelector.inputEndDate2.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            employeesViewModel.setEndDate(year,monthOfYear,dayOfMonth)
        }
        binding.includeDateSelector.btnDialogDateSelectorBack.setOnClickListener {
            binding.btnConfirmEmployees.visibility = View.VISIBLE
            binding.includeDateSelector.root.visibility = View.GONE
        }
        binding.includeDateSelector.btnDialogDateSelectorConfirm.setOnClickListener {
            Log.d("meow","test")
            if (employeesViewModel.checkForValidDates()) {
                (context as MainActivity).generateTipReport(employeesViewModel.employees.value!!,employeesViewModel.startDate.value!!,employeesViewModel.endDate.value!!)
                val hoursViewModel: HoursViewModel by activityViewModels()
                hoursViewModel.initializeTipReports(employeesViewModel.generateNewTipReports())
                binding.btnConfirmEmployees.visibility = View.VISIBLE
                findNavController().navigate(R.id.action_ListFrag_to_HoursFrag)
            }
            else {
                val toast = employeesViewModel.getDateValidityString()
                (context as MainActivity).makeToastMessage(toast)
            }
        }
    }

    // Updates Views
    private fun updateConfirmButtonVisibility() {
        if (employeesViewModel.checkForValidInputs()) {
            val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme)
            binding.btnConfirmEmployees.setBackgroundColor(sbGreen)
        }
        else {
            val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme)
            binding.btnConfirmEmployees.setBackgroundColor(wrmNeutral)
        }
    }

}