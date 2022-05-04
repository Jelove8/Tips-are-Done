package com.example.tipsaredone.views

import android.app.DatePickerDialog
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.DatePicker
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
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class EmployeeHoursFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentEmployeeHoursBinding? = null
    private val binding get() = _binding!!

    private var day = 0
    private var month = 0
    private var year = 0

    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0



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

        binding.button.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(context as MainActivity,this,year,month,day).show()
        }

        binding.btnConfirmHours.setOnClickListener {
            findNavController().navigate(R.id.action_EmployeeHoursFragment_to_inputTipsFragment)
        }
    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month + 1          // HELP! HELP! HELP ME!!!! AHHHH!!!!
        savedYear = year

        getDateTimeCalendar()
        binding.tvDate.text = "$savedMonth-$savedDay-$savedYear"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}