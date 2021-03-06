package com.example.tipsaredone.views

import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.databinding.FragmentEmployeesListBinding
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.MyEmployees
import com.example.tipsaredone.viewmodels.EmployeesViewModel

class EmployeeListFragment : Fragment() {

    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var employeeListAdapter: EmployeesAdapter

    private var _binding: FragmentEmployeesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        _binding = FragmentEmployeesListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize EmployeesViewModel
        val employeesVM: EmployeesViewModel by activityViewModels()
        employeesViewModel = employeesVM

        // Initialize views
        loadEmployeesFromStorage()
        checkForValidInputs()
        updateSumOfHours()

        // Employee RecyclerView
        employeeListAdapter = EmployeesAdapter( employeesViewModel.employees.value!!,

            // Click employee item to edit their name...
            itemClickCallback = fun(position: Int) {
                employeesViewModel.setEditingEmployeeBool(true)
                employeesViewModel.selectEmployee(position)
                showDialogView(position)
            },

            // When user inputs employee hours...
            textChangedCallback = fun(_: Int) {
                updateSumOfHours()
                checkForValidInputs()
            }
        )
        binding.rcyEmployees.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyEmployees.adapter = employeeListAdapter

        // Dialog View Logic
        binding.btnConfirmEmployeeDialog.setOnClickListener {
            val newName = binding.etEmployeeDialog.text
            if (newName.isNullOrEmpty()) {
                (context as MainActivity).makeToastMessage("A name is required.")
            }
            else {
                // Confirming edits to employee name.
                if (employeesViewModel.getEditingEmployeeBool()) {
                    employeeListAdapter.editEmployeeName(employeesViewModel.getSelectedPosition(), newName.toString())
                }
                // Confirming new employee name.
                else {
                    employeeListAdapter.addNewEmployee(Employee(newName.toString()))
                }
                checkForValidInputs()
                hideEmployeeDialog()
            }
        }
        binding.btnDeleteEmployeeDialog.setOnClickListener {
            employeeListAdapter.deleteEmployee(employeesViewModel.getSelectedPosition())
            updateSumOfHours()
            checkForValidInputs()
            hideEmployeeDialog()
        }
        binding.btnCancelEmployeeDialog.setOnClickListener {
            hideEmployeeDialog()
        }

        // Navigation Button
        binding.btnConfirmEmployees.setOnClickListener {
            if (employeesViewModel.getConfirmButtonBool()) {
                saveEmployeesToStorage()
                findNavController().navigate(R.id.action_EmployeeFragment_to_InputTipsFragment)
            }
            else {
                val output = employeesViewModel.checkForValidInputs()
                (context as MainActivity).makeToastMessage(output)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)  {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_frag_employees, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        return when (item.itemId) {
            // Add New Employee Button
            R.id.action_add_employee -> {
                if (binding.cnstEmployeeDialog.visibility == View.GONE) {
                    employeesViewModel.setEditingEmployeeBool(false)
                    showDialogView(null)
                    true
                }
                else {
                    hideEmployeeDialog()
                    false
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Internal Storage
    private fun loadEmployeesFromStorage() {
        val myEmployees = MyEmployees()
        val data = myEmployees.loadEmployeeNamesFromInternalStorage(context as MainActivity)
        employeesViewModel.initializeEmployees(data)
    }
    private fun saveEmployeesToStorage() {
        MyEmployees().saveEmployeeNamesToInternalStorage(employeesViewModel.employees.value!!,context as MainActivity)
    }

    // Dialog Box
    private fun showDialogView(position: Int?) {
        if (position != null) {
            // Prepares for the editing of an employee.
            binding.btnDeleteEmployeeDialog.visibility = View.VISIBLE
            binding.tvEmployeeDialog.setText(R.string.edit_employee)
            binding.etEmployeeDialog.setText(employeesViewModel.employees.value!![position].name)
            binding.tvEmployeeDialogBackground.visibility = View.VISIBLE
            binding.cnstEmployeeDialog.visibility = View.VISIBLE
        }
        else {
            // Prepares for the adding of a new employee.
            binding.btnDeleteEmployeeDialog.visibility = View.GONE
            binding.tvEmployeeDialog.setText(R.string.new_employee)
            binding.etEmployeeDialog.text.clear()
            binding.tvEmployeeDialogBackground.visibility = View.VISIBLE
            binding.cnstEmployeeDialog.visibility = View.VISIBLE
        }
    }
    private fun hideEmployeeDialog() {
        binding.cnstEmployeeDialog.visibility = View.GONE
        binding.tvEmployeeDialogBackground.visibility = View.GONE
    }

    // Updates Views
    private fun updateSumOfHours() {
        binding.tvTotalHours.text = employeesViewModel.getSumHours().toString()
    }
    private fun checkForValidInputs() {     // Checks if user should be able to click the Confirm button.
        employeesViewModel.checkForValidInputs()
        displayConfirmButton()
    }
    private fun displayConfirmButton(){
        // https://stackoverflow.com/questions/23517879/set-background-color-programmatically
        val button: View = binding.btnConfirmEmployees

        if (employeesViewModel.getConfirmButtonBool()) {     // Button is clickable and visible.
            val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme)
            button.setBackgroundColor(sbGreen)
        }
        else {  // Button is not clickable and is hidden.
            val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme)
            button.setBackgroundColor(wrmNeutral)
        }
    }
}