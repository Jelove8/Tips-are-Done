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
import com.example.tipsaredone.viewmodels.EmployeeListViewModel
import com.example.tipsaredone.viewmodels.TipsViewModel
import java.math.BigDecimal
import java.math.RoundingMode

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

        val employeeListViewModel: EmployeeListViewModel by activityViewModels()
        val tipsViewModel: TipsViewModel by activityViewModels()
        val distributionViewModel: DistributionViewModel by activityViewModels()

        val tipCalculations = TipCalculations()

        // Displaying tipRate
        val tipRate = tipCalculations.getTipRate(employeeListViewModel.sumHours.value!!, tipsViewModel.getTotalTips())
        val roundedTipRate = BigDecimal(tipRate).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding.tvTipRate.text = roundedTipRate

        // Calculating & Distributing tips
        tipCalculations.distributeTips(employeeListViewModel.employees.value!!)

        //  CALCULATE TIPS BEFORE THIS LINE
        //  SET TIP VALUES FOR EACH EMPLOYEE OBJECT
        distributionAdapter = DistributionAdapter(employeeListViewModel.employees.value!!)

        // Populating recycler view
        binding.rcyTipDistribution.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyTipDistribution.adapter = distributionAdapter

        binding.btnSaveEmployees.setOnClickListener {
            employeeListViewModel.setInitialUse(true)
            findNavController().navigate(R.id.action_outputTipsFragment_to_EmployeeFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}