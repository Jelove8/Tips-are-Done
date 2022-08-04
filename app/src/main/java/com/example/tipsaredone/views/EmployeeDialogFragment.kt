package com.example.tipsaredone.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.IndividualReportsAdapter
import com.example.tipsaredone.databinding.FragmentEmployeeDialogBinding
import com.example.tipsaredone.model.NewEmployee
import com.example.tipsaredone.viewmodels.EmployeesViewModel

class EmployeeDialogFragment : Fragment() {

    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var individualReportAdapter: IndividualReportsAdapter

    private var _binding: FragmentEmployeeDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Getting the selected employee from ViewModel.
        val employeeVM: EmployeesViewModel by activityViewModels()
        employeesViewModel = employeeVM
        val selectedEmployee = employeesViewModel.selectedEmployee.value
        initializeViews(selectedEmployee)

        /**
         * ITEMCLICK:   For existing employees, user can collect/uncollect distributed tips.
         * VISIBILITY:  Only visible when editing an exisiting employeee.
         */
        if (selectedEmployee != null) {
            individualReportAdapter = IndividualReportsAdapter( employeesViewModel.selectedEmployee.value!!.tipReports,
                collectClickCallback = fun(position: Int) {
                  individualReportAdapter.collectTips(position)
                },
                uncollectClickCallback = fun(position: Int) {
                    individualReportAdapter.uncollectTips(position)
                }
            )
            binding.rcyIndivTipReports.layoutManager = LinearLayoutManager(context as MainActivity)
            binding.rcyIndivTipReports.adapter = individualReportAdapter
        }

        /**
         * BUTTON:  Delete an existing employee, then navigate back to EmployeeListFragment.
         */
        binding.btnDialogDelete.setOnClickListener {
            employeesViewModel.employees.value!!.remove(selectedEmployee)
            findNavController().navigate(R.id.action_employeeDialogFragment_to_ListFragment)
        }

        /**
         * BUTTON:  Confirm employee edits, then navigate back to EmployeeListFragment.
         */
        binding.btnDialogConfirm.setOnClickListener {
            val newName = binding.etDialogBox.text

            if (newName.isNullOrEmpty()) {
                val toast = resources.getString(R.string.employee_name_required)
                (context as MainActivity).makeToastMessage(toast)
            }
            else {
                // Adds a new employee to EmployeesViewModel.
                if (selectedEmployee == null) {
                    val newEmployee = NewEmployee(employeesViewModel.generateUniqueID(),newName.toString())
                    employeesViewModel.employees.value!!.add(newEmployee)
                }
                // Edits existing employee in EmployeesViewModel.
                else {
                    employeesViewModel.employees.value!!.forEach {
                        if (it.id == employeesViewModel.selectedEmployee.value!!.id) {
                            it.name = newName.toString()
                        }
                    }
                }
                findNavController().navigate(R.id.action_employeeDialogFragment_to_ListFragment)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeViews(selectedEmployee: NewEmployee?) {
        if (selectedEmployee == null) {
            binding.tvDialogBox.text = resources.getString(R.string.new_employee)
            binding.btnDialogDelete.visibility = View.GONE
            binding.etDialogBox.text.clear()
            binding.rcyIndivTipReports.visibility = View.GONE
        }
        else {
            binding.tvDialogBox.text = "Editing: ${selectedEmployee.name}"
            binding.btnDialogDelete.visibility = View.VISIBLE
            binding.etDialogBox.setText(selectedEmployee.name)
            binding.rcyIndivTipReports.visibility = View.VISIBLE
        }

        binding.etDialogBox.doAfterTextChanged {
            updateConfirmButtonVisibility()
        }
        updateConfirmButtonVisibility()
    }
    private fun updateConfirmButtonVisibility() {
        if (binding.etDialogBox.text.isNotEmpty()) {
            val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme)
            binding.btnDialogConfirm.setBackgroundColor(sbGreen)
        }
        else {
            val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme)
            binding.btnDialogConfirm.setBackgroundColor(wrmNeutral)
        }
    }
}