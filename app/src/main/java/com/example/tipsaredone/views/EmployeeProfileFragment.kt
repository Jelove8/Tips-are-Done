package com.example.tipsaredone.views

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.IndividualReportsAdapter
import com.example.tipsaredone.databinding.FragmentEmployeeProfileBinding
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.viewmodels.EmployeesViewModel

class EmployeeProfileFragment : Fragment() {

    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var individualReportAdapter: IndividualReportsAdapter

    private var _binding: FragmentEmployeeProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmployeeProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * INIT:   Initializing EmployeesViewModel & this fragment's views.
         */
        val employeeVM: EmployeesViewModel by activityViewModels()
        employeesViewModel = employeeVM
        val selectedEmployee = employeesViewModel.selectedEmployee.value!!
        initializeViews(selectedEmployee)

        /**
         * ITEMCLICK:   For existing employees, user can collect/uncollect distributed tips.
         * VISIBILITY:  Only visible when editing an exisiting employeee.
         */
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

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_delete_employee, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_delete_selected_employee -> {
                        if (!employeesViewModel.deleteEmployeeDialogShowing.value!!) {
                            showDeleteEmployeeDialog()
                            true
                        }
                        else {
                            hideDeleteEmployeeDialog()
                            false
                        }
                    }
                    else ->  false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        /**
         * BUTTON:  Confirm employee edits, then navigate back to EmployeeListFragment.
         */
        binding.btnDialogConfirm.setOnClickListener {
            val newName = binding.etEmployeeProfile.text

            if (newName.isNullOrEmpty()) {
                val toast = resources.getString(R.string.employee_name_required)
                (context as MainActivity).makeToastMessage(toast)
            }
            else {
                employeesViewModel.employees.value!!.forEach {
                    if (it.id == employeesViewModel.selectedEmployee.value!!.id) {
                        it.name = newName.toString()
                    }
                }
                (context as MainActivity).getDatabaseModel().updateEmployee(employeesViewModel.selectedEmployee.value!!)
                // Navigating back to EmployeeListFragment.
                findNavController().navigate(R.id.action_EmployeeDialogFrag_to_EmployeeListFrag)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeViews(selectedEmployee: Employee) {
        binding.tvEmployeeProfile.text = selectedEmployee.name
        binding.etEmployeeProfile.setText(selectedEmployee.name)
        binding.etEmployeeProfile.doAfterTextChanged {
            updateConfirmButtonVisibility()
        }
        updateConfirmButtonVisibility()
    }
    private fun updateConfirmButtonVisibility() {
        if (binding.etEmployeeProfile.text.isNotEmpty()) {
            val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme)
            binding.btnDialogConfirm.setBackgroundColor(sbGreen)
        }
        else {
            val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme)
            binding.btnDialogConfirm.setBackgroundColor(wrmNeutral)
        }
    }

    private fun showDeleteEmployeeDialog() {
        employeesViewModel.setDeleteEmployeeDialogShowing(true)
        val selectedEmployee = employeesViewModel.selectedEmployee.value!!
        binding.btnDialogConfirm.visibility = View.GONE
        binding.includeDeleteEmployeeDialog.root.visibility = View.VISIBLE
        binding.includeDeleteEmployeeDialog.tvDialogDeleteEmployeeHeader.text = "Delete ${selectedEmployee.name} ?"
        binding.includeDeleteEmployeeDialog.cnstDialogDeleteEmployee.setOnClickListener {
            hideDeleteEmployeeDialog()
        }
        binding.includeDeleteEmployeeDialog.btnDialogDeleteEmployeeCancel.setOnClickListener {
            hideDeleteEmployeeDialog()
        }
        binding.includeDeleteEmployeeDialog.btnDialogDeleteEmployeeConfirm.setOnClickListener {
            employeesViewModel.employees.value!!.remove(selectedEmployee)
            (context as MainActivity).getDatabaseModel().deleteEmployee(selectedEmployee)
            findNavController().navigate(R.id.action_EmployeeDialogFrag_to_EmployeeListFrag)
        }
    }
    private fun hideDeleteEmployeeDialog() {
        employeesViewModel.setDeleteEmployeeDialogShowing(false)
        binding.btnDialogConfirm.visibility = View.VISIBLE
        binding.includeDeleteEmployeeDialog.root.visibility = View.GONE
        binding.btnDialogConfirm.visibility = View.VISIBLE
    }
}