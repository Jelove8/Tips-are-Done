package com.example.tipsaredone.views

import android.os.Bundle
import android.provider.ContactsContract
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
import com.example.tipsaredone.model.DatabaseModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import java.util.*

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
         * ITEMCLICK:  Navigate to EmployeeProfileFragment.
         */
        employeesAdapter = EmployeesAdapter(employeesViewModel.employees.value!!,
            itemClickCallback = fun(position: Int) {
                showEditEmployeeDialog(employeesViewModel.employees.value!![position])
            },
            textChangedCallback = fun(_: Int) {
                updateSumOfHoursHeader()
            }
            )
        binding.rcyEmployeeList.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyEmployeeList.adapter = employeesAdapter

        /**
         * INIT:  Reads employees from database.
         */
        (context as MainActivity).readEmployeesFromDatabase(employeesAdapter)

        /**
         * BUTTON:  Show new employee dialog box.
         */
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_new_employee, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_employee -> {
                        if (!employeesViewModel.newEmployeeDialogShowing) {
                            showNewEmployeeDialog()
                            true
                        }
                        else if (employeesViewModel.newEmployeeDialogShowing) {
                            hideEmployeeDialog(false)
                            false
                        }
                        else {
                            false
                        }
                    }
                    R.id.action_settings -> {
                        findNavController().navigate(R.id.action_EmployeeListFragment_to_SettingsFragment)
                        true
                    }
                    else ->  false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.btnConfirmCollection.setOnClickListener {
            if (employeesViewModel.datePickerDialogShowing) {
                if (employeesViewModel.startDate.value!!.isBefore(employeesViewModel.endDate.value!!)) {
                    findNavController().navigate(R.id.action_EmployeeListFragment_to_tipCollectionFragment)
                }
                else {
                    (context as MainActivity).makeToastMessage(resources.getString(R.string.invalid_dates2))
                }
            }
            else {
                showDatePicker()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        (context as MainActivity).displayNavbar(true)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * DIALOG:  Add or Edit Employee.
     */
    private fun showNewEmployeeDialog() {
        if (!employeesViewModel.editEmployeeDialogShowing && !employeesViewModel.datePickerDialogShowing) {
            employeesViewModel.newEmployeeDialogShowing = true

            val dialogBox = binding.includeNewEmployeeDialog
            dialogBox.root.visibility = View.VISIBLE
            dialogBox.etDialogNewEmployee.text.clear()
            binding.btnConfirmCollection.visibility = View.GONE

            dialogBox.etDialogNewEmployee.doAfterTextChanged {
                if (it.isNullOrEmpty()) {
                    dialogBox.btnDialogNewEmployeeConfirm.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme))
                }
                else {
                    dialogBox.btnDialogNewEmployeeConfirm.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme))
                }
            }

            dialogBox.btnDialogNewEmployeeCancel.setOnClickListener {
                hideEmployeeDialog(false)
            }
            dialogBox.btnDialogNewEmployeeConfirm.setOnClickListener {
                val newName = dialogBox.etDialogNewEmployee.text
                if (newName != null) {
                    val newEmployee = Employee(newName.toString(),generateEmployeeUID())
                    employeesAdapter.addNewEmployee(newEmployee)
                    DatabaseModel().addOrEditEmployee(newEmployee)
                    hideEmployeeDialog(false)
                }
                else {
                    (context as MainActivity).makeToastMessage(resources.getString(R.string.employee_name_required))
                }
            }
        }

    }
    private fun showEditEmployeeDialog(selectedEmployee: Employee) {
        if (!employeesViewModel.newEmployeeDialogShowing && !employeesViewModel.datePickerDialogShowing) {
            employeesViewModel.editEmployeeDialogShowing = true

            val dialogBox = binding.includeEditEmployeeDialog
            dialogBox.root.visibility = View.VISIBLE
            dialogBox.etEditEmployeeDialog.setText(selectedEmployee.name)
            binding.btnConfirmCollection.visibility = View.GONE

            dialogBox.etEditEmployeeDialog.doAfterTextChanged {
                if (it.isNullOrEmpty()) {
                    dialogBox.btnEditEmployeeDialogConfirm.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme))
                }
                else {
                    dialogBox.btnEditEmployeeDialogConfirm.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme))
                }
            }

            dialogBox.btnDialogEditEmployeeCancel.setOnClickListener {
                hideEmployeeDialog(true)
            }
            dialogBox.btnEditEmployeeDialogDelete.setOnClickListener {
                employeesAdapter.deleteEmployee(selectedEmployee)
                DatabaseModel().deleteExistingEmployee(selectedEmployee)
                hideEmployeeDialog(true)
            }
            dialogBox.btnEditEmployeeDialogConfirm.setOnClickListener {
                val newName = dialogBox.etEditEmployeeDialog.text
                if (newName != null) {
                    selectedEmployee.name = newName.toString()
                    employeesAdapter.editEmployee(selectedEmployee)
                    DatabaseModel().addOrEditEmployee(selectedEmployee)
                    hideEmployeeDialog(true)
                }
                else {
                    (context as MainActivity).makeToastMessage(resources.getString(R.string.employee_name_required))
                }
            }
        }
    }
    private fun hideEmployeeDialog(boolean: Boolean) {
        if (boolean) {
            binding.includeEditEmployeeDialog.root.visibility = View.GONE
            employeesViewModel.editEmployeeDialogShowing = false
        }
        else {
            binding.includeNewEmployeeDialog.root.visibility = View.GONE
            employeesViewModel.newEmployeeDialogShowing = false
        }
        binding.btnConfirmCollection.visibility = View.VISIBLE
    }
    private fun generateEmployeeUID(): String {
        var uniqueID = UUID.randomUUID().toString()
        employeesViewModel.employees.value!!.forEach {
            if (uniqueID == it.id) {
                uniqueID = generateEmployeeUID()
            }
        }
        return uniqueID
    }

    private fun showDatePicker() {
        binding.includeDatePickerDialog.root.visibility = View.VISIBLE
        employeesViewModel.datePickerDialogShowing = true

        val startDate = employeesViewModel.startDate.value!!
        val endDate = employeesViewModel.endDate.value!!

        binding.includeDatePickerDialog.startDatePicker.init(startDate.year,startDate.monthValue - 1,startDate.dayOfMonth) { _, year, month, day ->
            employeesViewModel.setStartDate(year,month + 1,day)
        }
        binding.includeDatePickerDialog.endDatePicker.init(endDate.year,endDate.monthValue - 1,endDate.dayOfMonth) { _, year, month, day ->
            employeesViewModel.setEndDate(year,month + 1,day)
        }

        binding.includeDatePickerDialog.btnDatePickerDialogCancel.setOnClickListener {
            binding.includeDatePickerDialog.root.visibility = View.GONE
            employeesViewModel.datePickerDialogShowing = false
        }

    }



    private fun updateSumOfHoursHeader() {
        val newSum = employeesAdapter.getSumOfHours()
        binding.tvEmployeeHoursSumValue.text = newSum.toString()
    }

























}