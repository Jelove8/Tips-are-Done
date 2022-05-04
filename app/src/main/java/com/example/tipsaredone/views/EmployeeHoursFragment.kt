package com.example.tipsaredone.views

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.EmployeeHoursAdapter
import com.example.tipsaredone.databinding.FragmentEmployeeHoursBinding
import com.example.tipsaredone.viewmodels.EmployeeHoursViewModel
import com.example.tipsaredone.viewmodels.EmployeeListViewModel
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

    private var dateToSelect: Boolean = false // false = startdate, true = enddate


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

        updateDateText(false)
        updateDateText(true)

        binding.etStartDate.setOnClickListener {
            getDateTimeCalendar()
            dateToSelect = false
            DatePickerDialog(context as MainActivity,this,year,month,day).show()
        }

        binding.etEndDate.setOnClickListener {
            getDateTimeCalendar()
            dateToSelect = true
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
        val employeeHoursVM: EmployeeHoursViewModel by activityViewModels()
        savedDay = dayOfMonth
        savedMonth = month + 1          // HELP! HELP! HELP ME!!!! AHHHH!!!!
        savedYear = year

        getDateTimeCalendar()
        employeeHoursVM.setDate(savedDay,savedMonth,savedYear,dateToSelect)

        updateDateText(dateToSelect)

    }

    private fun updateDateText(dateSelected: Boolean) {
        val employeeHoursVM: EmployeeHoursViewModel by activityViewModels()
        when (dateSelected) {
            false -> {
                binding.etStartDate.text = employeeHoursVM.getDate(false)
            }
            true -> {
                binding.etEndDate.text = employeeHoursVM.getDate(true)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}