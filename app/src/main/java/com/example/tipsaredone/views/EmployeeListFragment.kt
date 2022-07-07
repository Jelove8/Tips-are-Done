package com.example.tipsaredone.views

import android.content.Context
import android.content.res.Configuration
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

        initializeMyEmployees(context as MainActivity)


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
            textChangedCallback = fun(_: Int) {
                setSumHours(employeesViewModel.getSumHours())                                       // Displaying previously stored value from view model.
                checkForValidInputs()                               // Checks if user should be able to click the Confirm button.
            }
        )

        // Displays the sum of employee hours, from the viewmodel.
        setSumHours(employeesViewModel.getSumHours())
        // If user inputs are valid, confirm button is clickable.
        checkForValidInputs()

        // Populating recycler view
        binding.rcyEmployees.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyEmployees.adapter = employeeListAdapter
        Log.d("Initial","Adapter set in fragment.")



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
            checkForValidInputs()
        }

        binding.btnDeleteEmployeeDialog.setOnClickListener {
            employeesViewModel.deleteSelectedEmployee()
            employeeListAdapter.setEmployeeAdapterData(employeesViewModel.employees.value!!)
            hideEmployeeDialog()
            checkForValidInputs()
            Log.d("debug","fragment function")
        }

        binding.btnCancelEmployeeDialog.setOnClickListener {
            hideEmployeeDialog()
            checkForValidInputs()
        }

        // Validating user inputs
        binding.btnConfirmEmployees.setOnClickListener {
            if (!employeeListAdapter.checkEmployees()) {
                (context as MainActivity).makeToastMessage("At least two employees must be entered.")
            }
            else if (!employeeListAdapter.checkForNullHours()) {
                (context as MainActivity).makeToastMessage("All hours must be filled.")
            }
            else if (visibleConfirmButton) {  // Proceed to next fragment.
                findNavController().navigate(R.id.action_EmployeeFragment_to_InputTipsFragment)
                MyEmployees().saveEmployeeNamesToInternalStorage(employeesViewModel.employees.value!!,context as MainActivity)
            }
            else {
                Log.d(MainActivity.MISC,"EmployeesListFrag-btnConfirmEmployees.onClickListen-elseBranch")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Configuration Changes
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {


        }
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

    // MyEmployees
    private fun initializeMyEmployees(context: Context) {
        myEmployees = MyEmployees()
        val data = myEmployees.loadEmployeeNamesFromInternalStorage(context)
        employeesViewModel.initializeEmployees(data)
    }

    // Displays sum of hours from view model.
    private fun setSumHours(newSum: Double) {
        binding.tvTotalHours.text =
            if (newSum == 0.00) { "0.00" }
            else { newSum.toString() }
    }

    private fun displayConfirmButton(){
        // https://stackoverflow.com/questions/23517879/set-background-color-programmatically
        val button: View = binding.btnConfirmEmployees

        if (visibleConfirmButton) {     // Button is clickable and visible.
            val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme)
            button.setBackgroundColor(sbGreen)
            button.isClickable = true
            Log.d("meow","Button Green")
        }
        else {  // Button is not clickable and is hidden.
            val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme)
            button.setBackgroundColor(wrmNeutral)
            button.isClickable = false
            Log.d("meow","Button Warm")
        }
    }

    private fun checkForValidInputs() {     // Checks if user should be able to click the Confirm button.
        visibleConfirmButton = if (employeeListAdapter.checkEmployees()) {
            employeeListAdapter.checkForNullHours()
        } else {
            false
        }
        displayConfirmButton()
    }



}