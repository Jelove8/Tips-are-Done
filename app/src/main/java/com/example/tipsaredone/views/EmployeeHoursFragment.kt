package com.example.tipsaredone.views

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.activities.MainActivity
import com.example.tipsaredone.adapters.HoursAdapter
import com.example.tipsaredone.databinding.FragmentEmployeeHoursBinding
import com.example.tipsaredone.viewmodels.DatePickerViewModel
import com.example.tipsaredone.viewmodels.HoursViewModel


class EmployeeHoursFragment : Fragment() {

    private lateinit var hoursViewModel: HoursViewModel
    private lateinit var datePickerViewModel: DatePickerViewModel

    private lateinit var hoursAdapter: HoursAdapter

    private var _binding: FragmentEmployeeHoursBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmployeeHoursBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val hoursVM: HoursViewModel by activityViewModels()
        hoursViewModel = hoursVM

        val datePickerVM: DatePickerViewModel by activityViewModels()
        datePickerViewModel = datePickerVM

        /**
         * BUTTON:  Show date picker dialog box.
         */
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_select_dates, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_select_dates -> {
                        if (!hoursViewModel.datePickerDialogShowing) {
                            displayDatePickerViews(true)
                        }
                        else {
                            displayDatePickerViews(false)
                        }
                        true
                    }
                    else ->  false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        /**
         * TEXT CHANGE CALLBACK:    Updates sum of hours displayed
         */
        hoursAdapter = HoursAdapter(hoursViewModel.employeeHours.value!!,
            textChangedCallback = fun(_: Int) {
                updateSumOfHoursHeader()
            }
        )
        binding.rcyEmployeeHours.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyEmployeeHours.adapter = hoursAdapter
        (context as MainActivity).initializeEmployeeHours(hoursAdapter)

        updateSumOfHoursHeader()
    }
    override fun onStart() {
        super.onStart()
        (context as MainActivity).displayNavbar(true)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Date Picker
    private fun displayDatePickerViews(isVisible: Boolean) {
        if (isVisible) {
            binding.btnDatePickerCancel.visibility = View.VISIBLE
            binding.includeDatePicker.root.visibility = View.VISIBLE
            binding.btnDatePickerConfirm.visibility = View.VISIBLE
            hoursViewModel.datePickerDialogShowing = true
            enableDatePickerLogic()
            (context as MainActivity).hideKeyboard()
            (context as MainActivity).displayNavbar(false)
        }
        else {
            binding.btnDatePickerCancel.visibility = View.GONE
            binding.includeDatePicker.root.visibility = View.GONE
            binding.btnDatePickerConfirm.visibility = View.GONE
            hoursViewModel.datePickerDialogShowing = false
            (context as MainActivity).displayNavbar(true)
        }
    }
    private fun enableDatePickerLogic() {
        val startDate = datePickerViewModel.startDate.value!!
        val endDate = datePickerViewModel.endDate.value!!

        /**
         * DATE PICKERS:    Initialized with current date
         */
        binding.includeDatePicker.startDatePicker.init(startDate.year,startDate.monthValue,startDate.dayOfMonth) { _, year, month, day ->
            datePickerViewModel.setStartDate(year,month + 1,day)
        }
        binding.includeDatePicker.endDatePicker.init(endDate.year,endDate.monthValue,endDate.dayOfMonth) { _, year, month, day ->
            datePickerViewModel.setEndDate(year,month + 1,day)
        }

        /**
         * BUTTONS: Confirm or cancel selected dates
         */
        binding.btnDatePickerCancel.setOnClickListener {
            displayDatePickerViews(false)
        }
        binding.btnDatePickerConfirm.setOnClickListener {
            if (checkForValidDates()) {
                val startDatePicker = binding.includeDatePicker.startDatePicker
                val endDatePicker = binding.includeDatePicker.endDatePicker

                val selectedStartDate = "${startDatePicker.year}-${startDatePicker.month + 1}-${startDatePicker.dayOfMonth}"
                val selectedEndDate = "${endDatePicker.year}-${endDatePicker.month + 1}-${endDatePicker.dayOfMonth}"
                val employeeHours = hoursAdapter.getListOfHours()
                (context as MainActivity).generateNewWeeklyReport(selectedStartDate,selectedEndDate,employeeHours)
                findNavController().navigate(R.id.action_employeeHours_toTipCollection)
            }
        }
    }

    // Misc
    private fun updateSumOfHoursHeader() {
        val newSum = hoursAdapter.getSumOfHours()
        (context as MainActivity).supportActionBar?.title = "Total Hours  |  $newSum"
    }
    private fun checkForValidDates(): Boolean {
        val startDate = datePickerViewModel.startDate.value!!
        val endDate = datePickerViewModel.endDate.value!!

        return if (startDate.isBefore(endDate)) {
            true
        }
        else {
            (context as MainActivity).makeToastMessage(resources.getString(R.string.invalid_dates2))
            false
        }
    }
}





















