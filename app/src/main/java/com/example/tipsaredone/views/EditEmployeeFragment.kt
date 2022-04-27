package com.example.tipsaredone.views

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.FragmentEditEmployeeBinding
import com.example.tipsaredone.viewmodels.EmployeeViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditEmployeeFragment : Fragment() {

    private var _binding: FragmentEditEmployeeBinding? = null
    private lateinit var editedName: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        val employeeVM: EmployeeViewModel by activityViewModels()

        val selectedEmployee = employeeVM.getSelectedEmployee()

        editedName = selectedEmployee.name
        binding.etEditName.setText(selectedEmployee.name)

        binding.etEditName.doAfterTextChanged {
            editedName = it.toString()
        }

        binding.btnDeleteEmployee.setOnClickListener {
            employeeVM.deleteSelectedEmployee()
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
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
        val employeeVM: EmployeeViewModel by activityViewModels()
        return when (item.itemId) {
            R.id.action_confirm_edits -> {
                if (editedName.isEmpty()) {
                    (context as MainActivity).makeToastMessage("Name must be filled out.")
                    false
                }
                else {
                    employeeVM.confirmEdits(editedName)
                    findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                    true
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}