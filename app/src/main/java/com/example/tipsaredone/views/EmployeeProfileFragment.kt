package com.example.tipsaredone.views

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
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

        // Initializing ViewModel.
        val employeeVM: EmployeesViewModel by activityViewModels()
        employeesViewModel = employeeVM

        // Initializing view based on selected employee.
        val selectedEmployeeMock = Employee(employeesViewModel.selectedEmployee.value!!.name,employeesViewModel.selectedEmployee.value!!.id)
        binding.tvEmployeeProfile.text = selectedEmployeeMock.name
        binding.etEmployeeProfile.setText(selectedEmployeeMock.name)
        binding.etEmployeeProfile.doAfterTextChanged {
            if (!it.isNullOrEmpty()) {
                selectedEmployeeMock.name = it.toString()
            }
            updateConfirmButtonVisibility()
        }
        Log.d("FirebaseDatabase","Employee Selected: ${selectedEmployeeMock.name}, ${selectedEmployeeMock.id}")

        /**
         * ITEMCLICK:   For existing employees, user can collect/uncollect distributed tips.
         */
        individualReportAdapter = IndividualReportsAdapter(employeesViewModel.selectedEmployee.value!!.tipReports,
            collectClickCallback = fun(position: Int) {
                individualReportAdapter.collectTips(position)
            },
            uncollectClickCallback = fun(position: Int) {
                individualReportAdapter.uncollectTips(position)
            }
        )
        binding.rcyIndivTipReports.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyIndivTipReports.adapter = individualReportAdapter

        /**
         * BUTTON:  Delete selected employee.
         */
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
            if (binding.etEmployeeProfile.text.isNullOrEmpty()) {
                val toast = resources.getString(R.string.employee_name_required)
                (context as MainActivity).makeToastMessage(toast)
            }
            else {
                selectedEmployeeMock.name = binding.etEmployeeProfile.text.toString()
                employeesViewModel.updateSelectedEmployee(selectedEmployeeMock)
                (context as MainActivity).updateExistingEmployee(selectedEmployeeMock)
                findNavController().navigate(R.id.action_EmployeeDialogFrag_to_EmployeeListFrag)
            }
        }
        updateConfirmButtonVisibility()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        val dialogBox = binding.includeDeleteEmployeeDialog
        dialogBox.root.visibility = View.VISIBLE
        employeesViewModel.setDeleteEmployeeDialogShowing(true)
        binding.btnDialogConfirm.visibility = View.GONE

        val selectedName = employeesViewModel.selectedEmployee.value!!.name
        val deleteMessage = resources.getString(R.string.delete_confirmation1) + selectedName + resources.getString(R.string.delete_confirmation2)
        dialogBox.tvDialogDeleteEmployee.text = deleteMessage

        /**
         * BUTTONS: Hide the Delete Confirmation dialog box.
         */
        dialogBox.cnstDialogDeleteEmployee.setOnClickListener {
            hideDeleteEmployeeDialog()
        }
        dialogBox.btnDialogDeleteEmployeeCancel.setOnClickListener {
            hideDeleteEmployeeDialog()
        }

        /**
         * BUTTON: Deletes an employee from database.
         */
        dialogBox.btnDialogDeleteEmployeeConfirm.setOnClickListener {
            val selectedEmployee = employeesViewModel.selectedEmployee.value!!
            employeesViewModel.employees.value!!.remove(selectedEmployee)
            (context as MainActivity).deleteExistingEmployee(selectedEmployee)
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