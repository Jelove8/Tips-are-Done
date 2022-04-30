package com.example.tipsaredone.views

import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.adapters.EmployeeHoursAdapter
import com.example.tipsaredone.databinding.FragmentEmployeeHoursBinding
import com.example.tipsaredone.viewmodels.EmployeeHoursViewModel
import com.example.tipsaredone.viewmodels.EmployeeViewModel

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

        val employeeListVM: EmployeeViewModel by activityViewModels()
        val employeeHoursVM: EmployeeHoursViewModel by activityViewModels()
        employeeHoursVM.setEmployeesList(employeeListVM.employeesList.value!!)

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
        employeeHoursAdapter.setEmployeeAdapterData(employeeListVM.employeesList.value!!, employeeHoursVM.employeeHours.value!!)
        binding.rcyEmployeeHours.adapter = employeeHoursAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}