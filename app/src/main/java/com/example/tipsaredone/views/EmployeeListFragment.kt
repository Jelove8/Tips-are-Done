package com.example.tipsaredone.views

import android.os.Bundle
import android.util.Log
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
import com.example.tipsaredone.activities.MainActivity
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.databinding.FragmentEmployeesListBinding
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.viewmodels.EmployeesViewModel

class EmployeeListFragment : Fragment() {

    private var _binding: FragmentEmployeesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var employeesAdapter: EmployeesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmployeesListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Initialize EmployeesViewModel
        val employeesVM: EmployeesViewModel by activityViewModels()
        employeesViewModel = employeesVM


        /**
         * ITEMCLICK:  Navigate to EmployeeProfileFragment to edit an employee.
         */
        employeesAdapter = EmployeesAdapter(employeesViewModel.employees.value!!,
            itemClickCallback = fun(position: Int) {
                navigateEditEmployee(position)
            })
        binding.rcyEmployeeList.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyEmployeeList.adapter = employeesAdapter

        /**
         * INIT:  Firebase Database - reading employees from database, then populating the adapter.
         */
        if (employeesAdapter.itemCount == 0) {
            (context as MainActivity).initializeEmployeesFromDatabase(employeesAdapter)
        }

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
                    R.id.action_add_employee -> {
                        if (!employeesViewModel.newEmployeeDialogShowing.value!!) {
                            showNewEmployeeDialog()
                            true
                        }
                        else if (employeesViewModel.newEmployeeDialogShowing.value!!) {
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
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * NAVIGATION:  To EmployeeHoursFragment
     */
    private fun navigateEditEmployee(index: Int) {
        employeesViewModel.selectEmployee(index)
        findNavController().navigate(R.id.action_employeeList_to_employeeProfile)
    }


    /**
     * DIALOG:  Add New Employee.
     */
    private fun showNewEmployeeDialog() {
        val dialogBox = binding.includeNewEmployeeDialog
        dialogBox.root.visibility = View.VISIBLE
        employeesViewModel.setNewEmployeeDialogShowing(true)
        employeesViewModel.setConfirmEmployeesButtonShowing(false)

        // If EditText is empty, the confirm button is hidden.
        dialogBox.etDialogNewEmployee.doAfterTextChanged {
            if (dialogBox.etDialogNewEmployee.text.isNullOrEmpty()) {
                val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme)
                dialogBox.btnDialogNewEmployeeConfirm.setBackgroundColor(wrmNeutral)
            }
            else {
                val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme)
                dialogBox.btnDialogNewEmployeeConfirm.setBackgroundColor(sbGreen)
            }
        }
        dialogBox.etDialogNewEmployee.text.clear()

        // If out-of-bounds area is clicked, dialog is hidden.
        dialogBox.cnstDialogNewEmployee.setOnClickListener {
            hideNewEmployeeDialog()
        }

        /**
         * BUTTON:  Confirm new Employee name, updating EmployeesAdapter & Database.
         */
        dialogBox.btnDialogNewEmployeeConfirm.setOnClickListener {
            if (dialogBox.etDialogNewEmployee.text.isNullOrEmpty()) {
                val toast = resources.getString(R.string.employee_name_required)
                (context as MainActivity).makeToastMessage(toast)
            }
            else {
                val newName = dialogBox.etDialogNewEmployee.text.toString()
                val newEmployee = Employee(newName,employeesViewModel.generateUniqueID())
                employeesAdapter.addNewEmployee(newEmployee)
                employeesViewModel.addIndividualTipReport(newEmployee)
                (context as MainActivity).addNewEmployeeToDatabase(newEmployee)
                hideNewEmployeeDialog()
            }
        }
    }
    private fun hideNewEmployeeDialog() {
        binding.includeNewEmployeeDialog.root.visibility = View.GONE
        employeesViewModel.setNewEmployeeDialogShowing(false)
        employeesViewModel.setConfirmEmployeesButtonShowing(true)
    }

    // Validity
    private fun checkForValidEmployees(): Boolean {
        return employeesAdapter.itemCount > 1
    }

}