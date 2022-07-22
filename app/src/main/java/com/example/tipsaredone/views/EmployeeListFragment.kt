package com.example.tipsaredone.views

import android.content.Context
import android.os.Bundle
import android.util.Log
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

    companion object {
        const val THIS: String = "EmployeeListFragment"
    }

    init {
        Log.d("Initial","Fragment initialized.")
    }

    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var employeeListAdapter: EmployeesAdapter
    private lateinit var myEmployees: MyEmployees

    private var _binding: FragmentEmployeesListBinding? = null
    private val binding get() = _binding!!

    private var visibleConfirmButton: Boolean = false

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

        (context as MainActivity).initializeMyEmployees()
        updateSumOfHours()

        // Adapter Logic
        employeeListAdapter = EmployeesAdapter(
            employeesViewModel.employees.value!!,

            // Click employee item to edit their name...
            itemClickCallback = fun(position: Int) {
                employeesViewModel.setEditingEmployeeBool(true)
                employeesViewModel.selectEmployee(position)
                showDialogView(position)
                checkForValidInputs()                               // Checks if user should be able to click the Confirm button.

            },

            // When user inputs employee hours...
            textChangedCallback = fun(_: Int, _: Double?) {
                updateSumOfHours()
                checkForValidInputs()                               // Checks if user should be able to click the Confirm button.\
            }
        )


        // If user inputs are valid, confirm button is clickable.
        checkForValidInputs()

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
                    // Updates the edited employee name & the adapter
                    employeeListAdapter.editEmployeeName(employeesViewModel.getSelectedPosition(), newName.toString())
                }
                else {
                    // Adds a new employee to viewmodel & updates the adapter.
                    employeeListAdapter.addNewEmployee(Employee(newName.toString()))
                }
                hideEmployeeDialog()
            }
            checkForValidInputs()
        }


        binding.btnDeleteEmployeeDialog.setOnClickListener {
            employeeListAdapter.deleteEmployee(employeesViewModel.getSelectedPosition())
            updateSumOfHours()
            hideEmployeeDialog()
            checkForValidInputs()
        }

        binding.btnCancelEmployeeDialog.setOnClickListener {
            hideEmployeeDialog()
        }

        // Validating user inputs
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


    private fun updateSumOfHours() {
        binding.tvTotalHours.text = employeesViewModel.getSumHours().toString()
    }

    private fun saveEmployeesToStorage() {
        MyEmployees().saveEmployeeNamesToInternalStorage(employeesViewModel.employees.value!!,context as MainActivity)
    }



}