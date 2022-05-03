package com.example.tipsaredone.views

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.EmployeeHoursAdapter
import com.example.tipsaredone.databinding.FragmentEmployeeHoursBinding
import com.example.tipsaredone.viewmodels.EmployeeHoursViewModel
import com.example.tipsaredone.viewmodels.EmployeeListViewModel

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

        val employeeListListVM: EmployeeListViewModel by activityViewModels()
        val employeeHoursVM: EmployeeHoursViewModel by activityViewModels()
        employeeHoursVM.setEmployeesList(employeeListListVM.employeesList.value!!)

        val employeeHoursAdapter = EmployeeHoursAdapter(

            // Navigating to EditEmployeeFragment
            itemClickCallback = fun(editable: Editable, id: String) {
                if (editable.isEmpty()) {
                    employeeHoursVM.setEmployeeHours(0.00,id)
                }
                else {
                    employeeHoursVM.setEmployeeHours(editable.toString().toDouble(),id)
                }
            }
        )

        binding.rcyEmployeeHours.layoutManager = LinearLayoutManager(context as MainActivity)
        employeeHoursAdapter.setEmployeeAdapterData(employeeListListVM.employeesList.value!!, employeeHoursVM.employeeHours.value!!)
        binding.rcyEmployeeHours.adapter = employeeHoursAdapter

        binding.btnConfirmHours.setOnClickListener {
            findNavController().navigate(R.id.action_EmployeeHoursFragment_to_inputTipsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}