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
import com.example.tipsaredone.viewmodels.EmployeeViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EmployeeListFragment : Fragment() {

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

        val employeeVM: EmployeeViewModel by activityViewModels()

        val employeeAdapter = EmployeesAdapter(

            // Navigating to EditEmployeeFragment
            itemClickCallback = fun(position: Int) {
                employeeVM.selectedIndex.value = position
                findNavController().navigate(R.id.action_empList_to_empEdit)
            }
        )

        // Populating employee list recycler
        binding.rcyEmployees.layoutManager = LinearLayoutManager(context as MainActivity)
        employeeAdapter.setEmployeeAdapterData(employeeVM.employeesList.value!!)
        binding.rcyEmployees.adapter = employeeAdapter

        // New Employee Logic
        binding.btnCancelNewEmployee.setOnClickListener {
            binding.etNewEmployeeName.text.clear()
            binding.cnstNewEmployee.visibility = View.GONE
            binding.btnConfirmEmployees.visibility = View.VISIBLE
        }
        binding.btnConfirmNewEmployee.setOnClickListener {
            if (binding.etNewEmployeeName.text.isNullOrEmpty()) {
                (context as MainActivity).makeToastMessage("A name must be entered.")
            }
            else {
                employeeVM.addNewEmployee(binding.etNewEmployeeName.text.toString())
                employeeAdapter.setEmployeeAdapterData(employeeVM.employeesList.value!!)
                binding.etNewEmployeeName.text.clear()
                binding.cnstNewEmployee.visibility = View.GONE
                binding.btnConfirmEmployees.visibility = View.VISIBLE
            }
        }

        // Navigating to TippableHoursFragment
        binding.btnConfirmEmployees.setOnClickListener {

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
                binding.cnstNewEmployee.visibility = View.VISIBLE
                binding.btnConfirmEmployees.visibility = View.GONE
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}