package com.example.tipsaredone.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.MainActivity
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.databinding.FragmentEmployeesBinding
import com.example.tipsaredone.model.MockData
import com.example.tipsaredone.viewmodels.EmployeeViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EmployeesFragment : Fragment() {

    private var _binding: FragmentEmployeesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEmployeesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val employeeVM: EmployeeViewModel by activityViewModels()

        // Initializing employee data in viewmodel
        val mockEmployees = MockData().getMockEmployees()
        employeeVM.initializeVM(mockEmployees)

        // Populating recycler
        binding.rcyEmployees.layoutManager = LinearLayoutManager(context as MainActivity)
        val newAdapter = EmployeesAdapter(employeeVM.employeesList.value!!)
        binding.rcyEmployees.adapter = newAdapter

        binding.btnConfirmEmployees.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}