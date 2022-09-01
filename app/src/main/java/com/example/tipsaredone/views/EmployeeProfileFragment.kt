package com.example.tipsaredone.views

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.activities.MainActivity
import com.example.tipsaredone.adapters.IndividualReportsAdapter
import com.example.tipsaredone.databinding.FragmentEmployeeProfileBinding
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.viewmodels.EmployeesViewModel

class EmployeeProfileFragment : Fragment() {

    private var _binding: FragmentEmployeeProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var individualReportAdapter: IndividualReportsAdapter

    private lateinit var selectedEmployee: Employee

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmployeeProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initializing ViewModel.
        val employeeVM: EmployeesViewModel by activityViewModels()
        employeesViewModel = employeeVM
        selectedEmployee = employeesViewModel.selectedEmployee.value!!

        binding.tvEmployeeProfile.text = selectedEmployee.name
        binding.etEmployeeProfile.setText(selectedEmployee.name)

        /**
         * ITEMCLICK:   Collect or uncollect tips from specific week.
         */
        individualReportAdapter = IndividualReportsAdapter(employeesViewModel.selectedEmployee.value!!.tipReports,
            collectClickCallback = fun(reportID: String) {
                collectTips(reportID,true)
            },
            uncollectClickCallback = fun(reportID: String) {
                collectTips(reportID,false)
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
                        if (!employeesViewModel.deleteEmployeeDialogShowing) {
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
         * BUTTON:  Confirm employee edits.
         */
        binding.btnDialogConfirm.setOnClickListener {
            if (binding.etEmployeeProfile.text.isNullOrEmpty()) {
                (context as MainActivity).makeToastMessage(resources.getString(R.string.employee_name_required))
            }
            else {
                (context as MainActivity).editExistingEmployee(selectedEmployee)
                findNavController().navigate(R.id.EmployeeListFragment)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun collectTips(selectedReportID: String, isCollecting: Boolean) {
        val employeeTipsCollectedBool = (context as MainActivity).collectEmployeeTips(selectedEmployee.id,selectedReportID,isCollecting)
        if (employeeTipsCollectedBool) {
            individualReportAdapter.collectTips(selectedReportID,isCollecting)
        }
    }

    private fun showDeleteEmployeeDialog() {
        val dialogBox = binding.includeDeleteEmployeeDialog
        dialogBox.root.visibility = View.VISIBLE
        binding.btnDialogConfirm.visibility = View.GONE
        employeesViewModel.deleteEmployeeDialogShowing = true

        val selectedName = employeesViewModel.selectedEmployee.value!!.name
        val deleteMessage = resources.getString(R.string.delete_confirmation1) + selectedName + resources.getString(R.string.delete_confirmation2)
        dialogBox.tvDialogDeleteEmployee.text = deleteMessage

        /**
         * BUTTONS: Cancel and Hide dialog box
         */
        dialogBox.btnDialogDeleteEmployeeCancel.setOnClickListener {
            hideDeleteEmployeeDialog()
        }
        dialogBox.btnDialogDeleteEmployeeCancel2.setOnClickListener {
            hideDeleteEmployeeDialog()
        }

        /**
         * BUTTON: Permanently delete employee
         */
        dialogBox.btnDialogDeleteEmployeeConfirm.setOnClickListener {
            (context as MainActivity).deleteExistingEmployee(selectedEmployee)
            findNavController().navigate(R.id.action_employeeProfile_to_employeeList)
        }
    }
    private fun hideDeleteEmployeeDialog() {
        employeesViewModel.deleteEmployeeDialogShowing = false
        binding.btnDialogConfirm.visibility = View.VISIBLE
        binding.includeDeleteEmployeeDialog.root.visibility = View.GONE
        binding.btnDialogConfirm.visibility = View.VISIBLE
    }
}