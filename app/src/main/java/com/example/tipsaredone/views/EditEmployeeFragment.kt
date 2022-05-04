package com.example.tipsaredone.views

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.FragmentEditEmployeeBinding
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.viewmodels.EmployeeListViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditEmployeeFragment : Fragment() {

    private var _binding: FragmentEditEmployeeBinding? = null
    private val binding get() = _binding!!

    private lateinit var selectedEmployee: Employee

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setHasOptionsMenu(true)
        _binding = FragmentEditEmployeeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val employeeListVM: EmployeeListViewModel by activityViewModels()

        selectedEmployee = employeeListVM.getSelectedEmployee()
        binding.etEditName.setText(selectedEmployee.name)

        binding.btnConfirmEmployee.setOnClickListener {

            var whiteSpaceOnlyCheck = true
            for (char in binding.etEditName.text) {
                whiteSpaceOnlyCheck = !char.isWhitespace()
            }

            if (binding.etEditName.text.isEmpty() || !whiteSpaceOnlyCheck) {
                (context as MainActivity).makeToastMessage("Name must be filled out.")
            }
            else {
                employeeListVM.deleteSelectedEmployee()
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)  {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_confirm_employee_edits, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val employeeListVM: EmployeeListViewModel by activityViewModels()

        return when (item.itemId) {
            R.id.action_delete_selected_employee -> {
                employeeListVM.deleteSelectedEmployee()
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
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