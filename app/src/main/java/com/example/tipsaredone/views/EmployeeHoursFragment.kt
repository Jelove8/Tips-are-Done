package com.example.tipsaredone.views

import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.adapters.HoursAdapter
import com.example.tipsaredone.databinding.FragmentEmployeeHoursBinding
import com.example.tipsaredone.databinding.FragmentEmployeesListBinding
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.HoursViewModel

class EmployeeHoursFragment : Fragment() {

    private lateinit var hoursViewModel: HoursViewModel
    private lateinit var hoursAdapter: HoursAdapter

    private var _binding: FragmentEmployeeHoursBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmployeeHoursBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize EmployeesViewModel
        val hoursVM: HoursViewModel by activityViewModels()
        hoursViewModel = hoursVM

        // Initializing Views
        updateConfirmButtonVisibility()
        updateSumOfHours()
        hoursAdapter = HoursAdapter(hoursViewModel.tipReports.value!!,

            // TextChanged: When employee hours are edited.
            textChangedCallback = fun(_: Int) {
                updateSumOfHours()
                updateConfirmButtonVisibility()
            }
        )
        binding.rcyEmployeeHours.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyEmployeeHours.adapter = hoursAdapter

        // Button: Confirm employees and navigate to next fragment.
        binding.btnEmployeeHoursConfirm.setOnClickListener {
            if (hoursViewModel.checkForValidHours()) {
                findNavController().navigate(R.id.action_HoursFrag_to_CollectionFrag)
            }
            else {
                val toast = resources.getString(R.string.employee_hours_required)
                (context as MainActivity).makeToastMessage(toast)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    // Updates Views
    private fun updateConfirmButtonVisibility() {
        if (hoursViewModel.checkForValidHours()) {
            val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme)
            binding.btnEmployeeHoursConfirm.setBackgroundColor(sbGreen)
        }
        else {
            val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme)
            binding.btnEmployeeHoursConfirm.setBackgroundColor(wrmNeutral)
        }
    }
    private fun updateSumOfHours() {
        binding.tvTotalHours.text = hoursViewModel.getSumOfHours().toString()
    }
}