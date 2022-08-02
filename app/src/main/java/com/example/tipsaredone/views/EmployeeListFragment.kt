package com.example.tipsaredone.views

import android.os.Bundle
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

        // Load saved data.
        if (checkInternetConnection()) {
            loadEmployeesFromFirebase()
        }
        else {
            loadEmployeesFromInternalStorage()
        }

        // Initializing Views
        updateConfirmButtonVisibility()
        updateSumOfHours()
        employeeListAdapter = EmployeesAdapter( employeesViewModel.employees.value!!,

            // Button: Edit existing employee.
            itemClickCallback = fun(position: Int) {
                showDialogView(position)
            },

            // TextChanged: When employee hours are edited.
            textChangedCallback = fun(_: Int) {
                updateSumOfHours()
                updateConfirmButtonVisibility()
            }
        )
        binding.rcyEmployees.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyEmployees.adapter = employeeListAdapter

        // Button: Add new employee.
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_frag_employees, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_employee -> {
                        if (binding.includeEmployeeDialog.root.visibility == View.GONE) {
                            showDialogView(null)
                            true
                        }
                        else {
                            hideEmployeeDialog()
                            false
                        }
                    }
                    else ->  false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Button: Confirm employees and navigate to next fragment.
        binding.btnConfirmEmployees.setOnClickListener {
            if (employeesViewModel.checkForValidInputs()) {
                (context as MainActivity).getRoughTipReport().employees = employeesViewModel.employees.value!!
                (context as MainActivity).saveEmployeesToStorage()
                findNavController().navigate(R.id.action_EmployeeFragment_to_InputTipsFragment)
            }
            else {
                val toast = employeesViewModel.getValidityString()
                (context as MainActivity).makeToastMessage(toast)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Internal Storage
    private fun loadEmployeesFromInternalStorage() {
        val data = (context as MainActivity).getEmployeesFromStorage()
        employeesViewModel.loadDataFromInternalStorage(data)
    }

    // Firebase Storage
    private fun checkInternetConnection(): Boolean {
        return false
    }
    private fun loadEmployeesFromFirebase() {
        TODO()
    }

    // Dialog Box
    private fun showDialogView(position: Int?) {
        if (position != null) {
            // Prepares for the editing of an employee.
            binding.includeEmployeeDialog.btnDialogDelete.visibility = View.VISIBLE
            binding.includeEmployeeDialog.tvDialogBox.setText(R.string.edit_employee)
            binding.includeEmployeeDialog.etDialogBox.setText(employeesViewModel.employees.value!![position].name)
        }
        else {
            // Prepares for the adding of a new employee.
            binding.includeEmployeeDialog.btnDialogDelete.visibility = View.GONE
            binding.includeEmployeeDialog.tvDialogBox.setText(R.string.new_employee)
            binding.includeEmployeeDialog.etDialogBox.text.clear()
        }
        binding.includeEmployeeDialog.root.visibility = View.VISIBLE
        binding.btnConfirmEmployees.visibility = View.GONE
        setDialogOnClickListeners(position)
    }
    private fun hideEmployeeDialog() {
        binding.includeEmployeeDialog.root.visibility = View.GONE
        binding.btnConfirmEmployees.visibility = View.VISIBLE
    }
    private fun setDialogOnClickListeners(position: Int?) {
        binding.includeEmployeeDialog.btnDialogConfirm.setOnClickListener {
            val newName = binding.includeEmployeeDialog.etDialogBox.text
            if (newName.isNullOrEmpty()) {
                (context as MainActivity).makeToastMessage("A name is required.")
            }
            else {
                if (position != null) {
                    employeeListAdapter.editEmployeeName(position,newName.toString())
                }
                else {
                    employeeListAdapter.addNewEmployee(Employee(newName.toString()))
                }
                updateConfirmButtonVisibility()
                hideEmployeeDialog()
            }
        }
        binding.includeEmployeeDialog.btnDialogDelete.setOnClickListener {
            employeeListAdapter.deleteEmployee(position!!)
            updateSumOfHours()
            updateConfirmButtonVisibility()
            hideEmployeeDialog()
        }
        binding.includeEmployeeDialog.btnDialogCancel.setOnClickListener {
            hideEmployeeDialog()
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
    private fun updateSumOfHours() {
        binding.tvTotalHours.text = employeesViewModel.getSumHours().toString()
    }
}