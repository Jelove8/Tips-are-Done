package com.example.tipsaredone.views

import android.os.Bundle
import android.util.Log
import android.view.*
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

        binding.etEditName.setText(selectedEmployee.name)

        binding.btnDeleteEmployee.setOnClickListener {
            employeeVM.deleteSelectedEmployee()
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}