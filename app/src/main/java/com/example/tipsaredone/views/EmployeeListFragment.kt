package com.example.tipsaredone.views

import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.databinding.FragmentEmployeesListBinding
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.viewmodels.EmployeesViewModel

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

        employeeListAdapter = EmployeesAdapter(employeesViewModel.employees.value!!,
            /**
             * ITEMCLICK:  Navigate to EmployeeProfileFragment to edit an employee.
             */
            itemClickCallback = fun(position: Int) {
                navigateEditEmployee(position)
            }
        )
        binding.rcyEmployeeList.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyEmployeeList.adapter = employeeListAdapter

        /**
         * BUTTON:  Navigate to EmployeeProfileFragment to add a new employee.
         */
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_new_employee, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    // All this code, just for a single button click...
                    R.id.action_add_employee -> {
                        if (!employeesViewModel.newEmployeeDialogShowing.value!! && !employeesViewModel.dateSelectionDialogShowing.value!!) {
                            showNewEmployeeDialog()
                            true
                        }
                        else if (employeesViewModel.newEmployeeDialogShowing.value!! && !employeesViewModel.dateSelectionDialogShowing.value!!) {
                            hideNewEmployeeDialog()
                            false
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
         * BUTTON:      Navigate to DatePickerFragment.
         * VISIBILITY:  Only visible/clickable if there are at least 2 existing employees.
         */
        binding.btnEmployeeListConfirm.setOnClickListener {
            if (checkForValidEmployees()) {
                findNavController().navigate(R.id.action_EmployeeListFrag_to_DatePickerFrag)
            }
            else {
                val toast = resources.getString(R.string.employees_list_invalid_string)
                (context as MainActivity).makeToastMessage(toast)
            }
        }
        updateConfirmButtonVisibility()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Employee Dialog
    private fun navigateEditEmployee(index: Int) {
        employeesViewModel.selectEmployee(index)
        findNavController().navigate(R.id.action_EmployeeListFrag_to_EmployeeProfileFrag)
    }
    private fun showNewEmployeeDialog() {
        employeesViewModel.setNewEmployeeDialogShowing(true)
        binding.btnEmployeeListConfirm.visibility = View.GONE
        binding.includeNewEmployee.root.visibility = View.VISIBLE
        binding.includeNewEmployee.etDialogNewEmployee.text.clear()
        binding.includeNewEmployee.etDialogNewEmployee.doAfterTextChanged {
            if (binding.includeNewEmployee.etDialogNewEmployee.text.isNullOrEmpty()) {
                val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme)
                binding.includeNewEmployee.btnDialogNewEmployeeConfirm.setBackgroundColor(wrmNeutral)
            }
            else {
                val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme)
                binding.includeNewEmployee.btnDialogNewEmployeeConfirm.setBackgroundColor(sbGreen)
            }
        }
        binding.includeNewEmployee.cnstDialogNewEmployee.setOnClickListener {
            hideNewEmployeeDialog()
        }
        binding.includeNewEmployee.btnDialogNewEmployeeConfirm.setOnClickListener {
            if (binding.includeNewEmployee.etDialogNewEmployee.text.isNullOrEmpty()) {
                val toast = resources.getString(R.string.employee_name_required)
                (context as MainActivity).makeToastMessage(toast)
            }
            else {
                employeeListAdapter.addNewEmployee(Employee(employeesViewModel.generateUniqueID(),binding.includeNewEmployee.etDialogNewEmployee.text.toString()))
                hideNewEmployeeDialog()
            }
        }
    }
    private fun hideNewEmployeeDialog() {
        employeesViewModel.setNewEmployeeDialogShowing(false)
        binding.includeNewEmployee.root.visibility = View.GONE
        binding.btnEmployeeListConfirm.visibility = View.VISIBLE
    }

    // Validity
    private fun checkForValidEmployees(): Boolean {
        return employeesViewModel.employees.value!!.size > 1
    }


    // Updates Views
    private fun updateConfirmButtonVisibility() {
        if (checkForValidEmployees()) {
            val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme)
            binding.btnEmployeeListConfirm.setBackgroundColor(sbGreen)
        }
        else {
            val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme)
            binding.btnEmployeeListConfirm.setBackgroundColor(wrmNeutral)
        }
    }
}