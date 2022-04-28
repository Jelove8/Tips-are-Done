package com.example.tipsaredone.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.EmployeeHoursAdapter
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.databinding.FragmentEmployeeHoursBinding
import com.example.tipsaredone.viewmodels.EmployeeHoursViewModel

class EmployeeHoursFragment : Fragment() {

    private var _binding: FragmentEmployeeHoursBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeHoursBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val employeeHoursVM: EmployeeHoursViewModel by activityViewModels()

        val employeeHoursAdapter = EmployeeHoursAdapter(

            // Navigating to EditEmployeeFragment
            itemClickCallback = fun(position: Int, hours: Double) {
               employeeHoursVM.setEmployeeHours(hours,position)
            }
        )

        binding.rcyEmployeeHours.layoutManager = LinearLayoutManager(context as MainActivity)
        employeeHoursAdapter.setEmployeeAdapterData(employeeHoursVM.getEmployeesList(), employeeHoursVM.getEmployeeHours())
        binding.rcyEmployeeHours.adapter = employeeHoursAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}