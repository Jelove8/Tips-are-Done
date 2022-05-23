package com.example.tipsaredone.views

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.databinding.FragmentEmployeesListBinding
import com.example.tipsaredone.viewmodels.EmployeeListViewModel
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EmployeeListFragment : Fragment() {

    private lateinit var employeeListViewModel: EmployeeListViewModel
    private lateinit var employeeListAdapter: EmployeesAdapter

    private var _binding: FragmentEmployeesListBinding? = null
    private val binding get() = _binding!!

    private val numpad: NumpadFragment = NumpadFragment(this)

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

        // Init
        val employeeListVM: EmployeeListViewModel by activityViewModels()
        employeeListViewModel = employeeListVM

        employeeListAdapter = EmployeesAdapter(

            // Click employee item to edit their name
            itemClickCallback = fun(position: Int) {
                employeeListViewModel.selectEmployee(position)
                showEditEmployeeDialog(position)
            },
            textChangedCallback = fun(sumHours: Double) {
                
                binding.tvTotalHours.text = sumHours.toString()
            }
        )

        employeeListAdapter.setEmployeeAdapterData(employeeListViewModel.employees.value!!)

        // Populating employee list recycler
        binding.rcyEmployees.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyEmployees.adapter = employeeListAdapter

        // Cancel employee dialog
        binding.btnCancelEmployeeDialog.setOnClickListener {
            hideEmployeeDialog()
        }


        // Navigating to EmployeeHoursFragment
        binding.btnConfirmEmployees.setOnClickListener {
            Log.d("Meow", employeeListVM.employees.value!![0].currentTippableHours.toString())
            findNavController().navigate(R.id.action_EmployeeFragment_to_InputTipsFragment)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)  {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_frag_employees, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_add_employee -> {
                if (binding.cnstEmployeeDialog.visibility == View.GONE) {
                    showNewEmployeeDialog()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showEditEmployeeDialog(position: Int) {
        binding.cnstEmployeeDialog.visibility = View.VISIBLE
        binding.tvEmployeeDialogBackground.visibility = View.VISIBLE
        binding.btnDeleteEmployeeDialog.visibility = View.VISIBLE
        binding.tvEmployeeDialog.text = "Edit Employee"
        binding.etEmployeeDialog.setText(employeeListViewModel.employees.value!![position].name)

        binding.btnConfirmEmployeeDialog.setOnClickListener {
            val newName = binding.etEmployeeDialog.text
            if (newName.isNullOrEmpty()) {
                (context as MainActivity).makeToastMessage("A name is required.")
            }
            else {
                employeeListViewModel.confirmSelectedEmployee(newName.toString())
                employeeListAdapter.setEmployeeAdapterData(employeeListViewModel.employees.value!!)
                hideEmployeeDialog()
            }
        }

        binding.btnDeleteEmployeeDialog.setOnClickListener {
            employeeListViewModel.deleteSelectedEmployee()
            employeeListAdapter.setEmployeeAdapterData(employeeListViewModel.employees.value!!)
            hideEmployeeDialog()
        }

    }

    private fun showNewEmployeeDialog() {
        binding.cnstEmployeeDialog.visibility = View.VISIBLE
        binding.tvEmployeeDialogBackground.visibility = View.VISIBLE
        binding.btnDeleteEmployeeDialog.visibility = View.GONE
        binding.tvEmployeeDialog.text = "New Employee"
        binding.etEmployeeDialog.text.clear()
        binding.btnConfirmEmployeeDialog.setOnClickListener {

            val newName = binding.etEmployeeDialog.text
            if (newName.isNullOrEmpty()) {
                (context as MainActivity).makeToastMessage("A name is required.")
            }
            else {
                employeeListViewModel.addNewEmployee(newName.toString())
                employeeListAdapter.setEmployeeAdapterData(employeeListViewModel.employees.value!!)
                hideEmployeeDialog()
            }
        }
    }

    private fun hideEmployeeDialog() {
        binding.cnstEmployeeDialog.visibility = View.GONE
        binding.tvEmployeeDialogBackground.visibility = View.GONE
    }



}