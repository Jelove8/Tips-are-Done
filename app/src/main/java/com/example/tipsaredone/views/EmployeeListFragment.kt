package com.example.tipsaredone.views

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.databinding.FragmentEmployeesListBinding
import com.example.tipsaredone.viewmodels.EmployeesViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EmployeeListFragment : Fragment() {

    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var employeeListAdapter: EmployeesAdapter

    private var _binding: FragmentEmployeesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setHasOptionsMenu(true)
        _binding = FragmentEmployeesListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initializing employees view model
        val employeesVM: EmployeesViewModel by activityViewModels()
        employeesViewModel = employeesVM

        // Initializing sum of hours display
        setSumHours()

        // Adapter Logic
        employeeListAdapter = EmployeesAdapter(

            // Click employee item to edit their name...
            itemClickCallback = fun(position: Int) {
                employeesViewModel.setEditingEmployeeBool(true)
                employeesViewModel.selectEmployee(position)
                showDialogView(position)
            },

            // When user inputs employee hours...
            textChangedCallback = fun(sumHours: Double) {
                employeesViewModel.setSumHours(sumHours)    // Setting sum of hours within view model.
                setSumHours()                               // Displaying previously stored value from view model.
            }
        )
        employeeListAdapter.setEmployeeAdapterData(employeesViewModel.employees.value!!)

        // Populating recycler view
        binding.rcyEmployees.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyEmployees.adapter = employeeListAdapter

        // Dialog View Logic
        binding.btnConfirmEmployeeDialog.setOnClickListener {
            val newName = binding.etEmployeeDialog.text
            if (newName.isNullOrEmpty()) {
                (context as MainActivity).makeToastMessage("A name is required.")
            }
            else {
                if (employeesViewModel.getEditingEmployeeBool()) {
                    // Updates the edited employee name & the adapter.
                    employeesViewModel.confirmSelectedEmployee(newName.toString())
                    employeeListAdapter.setEmployeeAdapterData(employeesViewModel.employees.value!!)
                }
                else {
                    // Adds a new employee to viewmodel & updates the adapter.
                    employeesViewModel.addNewEmployee(newName.toString())
                    employeeListAdapter.setEmployeeAdapterData(employeesViewModel.employees.value!!)
                }
                hideEmployeeDialog()
            }
        }

        binding.btnDeleteEmployeeDialog.setOnClickListener {
            employeesViewModel.deleteSelectedEmployee()
            employeeListAdapter.setEmployeeAdapterData(employeesViewModel.employees.value!!)
            hideEmployeeDialog()
        }

        binding.btnCancelEmployeeDialog.setOnClickListener {
            hideEmployeeDialog()
        }

        // Validating inputted employee data.
        binding.btnConfirmEmployees.setOnClickListener {
            if (!employeeListAdapter.checkEmployees()) {
                (context as MainActivity).makeToastMessage("At least two employees must be entered.")
            }
            else if (!employeeListAdapter.checkForNullHours()) {
                (context as MainActivity).makeToastMessage("All hours must be filled.")
            }
            else {  // Proceed to next fragment.
                findNavController().navigate(R.id.action_EmployeeFragment_to_InputTipsFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Menu Logic
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)  {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_frag_employees, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        // The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
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

    // Dialog Box
    private fun showDialogView(position: Int?) {
        if (position != null) {
            // Prepares for the editing of an employee.
            binding.btnDeleteEmployeeDialog.visibility = View.VISIBLE
            binding.tvEmployeeDialog.text = "Edit Employee"
            binding.etEmployeeDialog.setText(employeesViewModel.employees.value!![position!!].name)
        }
        else {
            // Prepares for the adding of a new employee.
            binding.btnDeleteEmployeeDialog.visibility = View.GONE
            binding.tvEmployeeDialog.text = "New Employee"
            binding.etEmployeeDialog.text.clear()
        }
    }
    private fun hideEmployeeDialog() {
        binding.cnstEmployeeDialog.visibility = View.GONE
        binding.tvEmployeeDialogBackground.visibility = View.GONE
    }

    // Displays sum of hours from view model.
    private fun setSumHours() {
        val sumOfHours = employeesViewModel.sumHours.value
        binding.tvTotalHours.text =
            if (sumOfHours == 0.00) { "0.00" }
            else { sumOfHours.toString() }
    }
}