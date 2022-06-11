package com.example.tipsaredone.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.DistributionAdapter
import com.example.tipsaredone.databinding.FragmentDistributionBinding
import com.example.tipsaredone.model.TipCalculations
import com.example.tipsaredone.viewmodels.DistributionViewModel
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.TipsViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.concurrent.schedule

class DistributionFragment : Fragment() {

    private var _binding: FragmentDistributionBinding? = null
    private val binding get() = _binding!!

    private lateinit var distributionAdapter: DistributionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDistributionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val employeesViewModel: EmployeesViewModel by activityViewModels()
        val tipsViewModel: TipsViewModel by activityViewModels()
        val distributionViewModel: DistributionViewModel by activityViewModels()

        val tipCalculations = TipCalculations()

        (context as MainActivity).hideToolbar()
        binding.loadingScreen.root.visibility = View.VISIBLE
        // Displaying loading screen
        Timer().schedule(1600){
            (context as MainActivity).runOnUiThread {
                (context as MainActivity).showToolbar()
                hideLoadingScreen()
            }
        }

        // Displaying tipRate
        val tipRate = tipCalculations.getTipRate(employeesViewModel.sumHours.value!!, tipsViewModel.getTotalTips())
        val roundedTipRate = BigDecimal(tipRate).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding.tvTipRate.text = roundedTipRate

        // Calculating & Distributing tips
        tipCalculations.distributeTips(employeesViewModel.employees.value!!)


        //  SET TIP VALUES FOR EACH EMPLOYEE OBJECT
        distributionAdapter = DistributionAdapter(employeesViewModel.employees.value!!)

        // Populating recycler view
        binding.rcyTipDistribution.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyTipDistribution.adapter = distributionAdapter

        binding.btnSaveEmployees.setOnClickListener {
            // Clearing inputted data, except for employee names
            employeesViewModel.clearEmployeeHoursAndDistributedTips()
            tipsViewModel.clearBillsList()
            (context as MainActivity).showTitleScreen(true)
            (context as MainActivity).saveEmployeeNamesToInternalStorage(employeesViewModel.employees.value!!)
            findNavController().navigate(R.id.action_outputTipsFragment_to_EmployeeFragment)
        }


    }


    private fun checkForRoundingErrors(employeesViewModel: EmployeesViewModel, tipsViewModel: TipsViewModel) {

        var roundedTotal = 0.0
        for (emp in employeesViewModel.employees.value!!) {
            roundedTotal += emp.distributedTips
        }

        // If error > 0, User will have some money leftover
        // If error = 0, User has no need to worry
        // If error < 0, A recalculation must occur
        // Pick employees at random to take away tip money


        var error = tipsViewModel.getTotalTips()

    }

    private fun hideLoadingScreen() {
        binding.loadingScreen.root.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}