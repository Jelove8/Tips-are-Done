package com.example.tipsaredone.views

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.activities.MainActivity
import com.example.tipsaredone.adapters.HoursAdapter
import com.example.tipsaredone.databinding.FragmentEmployeeHoursBinding
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.HoursViewModel
import io.grpc.Context


class EmployeeHoursFragment : Fragment() {

    private lateinit var hoursViewModel: HoursViewModel
    private lateinit var hoursAdapter: HoursAdapter

    private lateinit var employeesViewModel: EmployeesViewModel
    private var initBool: Boolean = true

    private var _binding: FragmentEmployeeHoursBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmployeeHoursBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Initialize EmployeesViewModel
        val hoursVM: HoursViewModel by activityViewModels()
        hoursViewModel = hoursVM

        val employeesVM: EmployeesViewModel by activityViewModels()
        employeesViewModel = employeesVM

        hoursAdapter = HoursAdapter(employeesViewModel.individualTipReports.value!!,
            // TextChanged: When employee hours are edited.
            textChangedCallback = fun(_: Int) {
                updateSumOfHoursHeader()
            }
        )
        binding.rcyEmployeeHours.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyEmployeeHours.adapter = hoursAdapter

        if (hoursAdapter.itemCount == 0) {
            employeesViewModel.initializeTipReports()
        }
        updateSumOfHoursHeader()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_do_tips, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_do_tips -> {
                        if (binding.includeDatePicker.root.visibility == View.VISIBLE) {
                            displayDatePicker(false)
                        }
                        else {
                            if (checkForValidEmployeesAndHours()) {
                                displayDatePicker(true)
                            }
                        }
                        true
                    }
                    else ->  false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    // Updates Views
    private fun displayDatePicker(isVisible: Boolean) {
        if (isVisible) {
            (context as MainActivity).hideKeyboard()
            binding.btnDatePickerCancel.visibility = View.VISIBLE
            binding.includeDatePicker.root.visibility = View.VISIBLE
            binding.btnDatePickerConfirm.visibility = View.VISIBLE
            (context as MainActivity).displayNavbar(false)

            binding.btnDatePickerCancel.setOnClickListener {
                displayDatePicker(false)
            }
            binding.includeDatePicker.cnstDatePicker.setOnClickListener {
                displayDatePicker(false)
            }
            binding.btnDatePickerConfirm.setOnClickListener {
                (context as MainActivity).convertEmployeesToJson(employeesViewModel.employees.value!!)
                (context as MainActivity).navigateToReportActivity()
            }
        }
        else {
            binding.btnDatePickerCancel.visibility = View.GONE
            binding.includeDatePicker.root.visibility = View.GONE
            binding.btnDatePickerConfirm.visibility = View.GONE
            (context as MainActivity).displayNavbar(true)
        }
    }


    private fun updateSumOfHoursHeader() {
        val newSum = hoursAdapter.getSumOfHours()
        (context as MainActivity).supportActionBar?.title = "Total Hours  |  $newSum"
    }

    private fun checkForValidEmployeesAndHours(): Boolean {
        return if (employeesViewModel.employees.value!!.size < 2) {
            (context as MainActivity).makeToastMessage(resources.getString(R.string.employees_list_invalid_string))
            false
        }
        else if (hoursAdapter.getSumOfHours() == 0.0) {
            (context as MainActivity).makeToastMessage(resources.getString(R.string.employee_hours_required))
            false
        }
        else {
            true
        }
    }











































}