package com.example.tipsaredone.views

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.FragmentEmployeePageBinding
import com.example.tipsaredone.viewmodels.EmployeePageViewModel


class EmployeePageFragment : Fragment() {

    private var _binding: FragmentEmployeePageBinding? = null
    private val binding get() = _binding!!


    private lateinit var employeeProfileViewModel: EmployeePageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentEmployeePageBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val employeeProfileVM: EmployeePageViewModel by activityViewModels()
        employeeProfileViewModel = employeeProfileVM

        initializeViews()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)  {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_confirm_employee_edits, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Delete Selected Employee
            R.id.action_delete_selected_employee -> {
                Log.d("testss","meow2")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeViews() {
        val selectedEmployee = employeeProfileViewModel.getEmployee()
        if (selectedEmployee == null) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "New Employee"
            binding.etEmployeeProfile.text.clear()
        }
        else {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = ""
            binding.etEmployeeProfile.setText(selectedEmployee.name)
        }

    }

}