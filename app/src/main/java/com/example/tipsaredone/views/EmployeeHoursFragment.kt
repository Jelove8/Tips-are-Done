package com.example.tipsaredone.views

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
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

    private var selectStartDate: Boolean = true // false = enddate, true = startdate


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
        employeeHoursVM.setEmployeesList(employeeListListVM.employees.value!!)

        
        updateDateDisplays(null)

        binding.etStartDate.setOnClickListener {
            getDateTimeCalendar()
            selectStartDate = true
            DatePickerDialog(context as MainActivity,this,year,month,day).show()
        }

        binding.etEndDate.setOnClickListener {
            getDateTimeCalendar()
            selectStartDate = false
            DatePickerDialog(context as MainActivity,this,year,month,day).show()
        }

        binding.btnConfirmHours.setOnClickListener {

        }
    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }
    private fun updateDateDisplays(boolean: Boolean?) {
        val employeeHoursVM: EmployeeHoursViewModel by activityViewModels()
        when (boolean) {
            true -> {
                binding.etStartDate.text = employeeHoursVM.getDateString(true)
            }
            false -> {
                binding.etEndDate.text = employeeHoursVM.getDateString(false)
            }
            else -> {
                binding.etStartDate.text = employeeHoursVM.getDateString(true)
                binding.etEndDate.text = employeeHoursVM.getDateString(false)
            }
        }
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val employeeHoursVM: EmployeeHoursViewModel by activityViewModels()
        savedDay = dayOfMonth
        savedMonth = month          // HELP! HELP! HELP ME!!!! AHHHH!!!!
        savedYear = year

        getDateTimeCalendar()
        employeeHoursVM.setDate(savedDay,savedMonth,savedYear,selectStartDate)
        updateDateDisplays(selectStartDate)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}