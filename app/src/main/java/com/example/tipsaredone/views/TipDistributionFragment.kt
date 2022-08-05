package com.example.tipsaredone.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.DistributionAdapter
import com.example.tipsaredone.databinding.FragmentTipDistributionBinding
import com.example.tipsaredone.viewmodels.TipCollectionViewModel
import kotlin.math.absoluteValue

class TipDistributionFragment : Fragment() {

    private var _binding: FragmentTipDistributionBinding? = null
    private val binding get() = _binding!!

    private lateinit var distributionAdapter: DistributionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTipDistributionBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Distributing Tips
        val weeklyTipReport = (context as MainActivity).getWeeklyTipReport()
        weeklyTipReport.distributeTips()

        if (weeklyTipReport.majorRoundingError != null) {
            showRoundingErrorDialog()
        }
        val roundedTipRate = (weeklyTipReport.tipRate * 100).toInt().toDouble() / 100
        binding.tvTipRate.text = "$ $roundedTipRate"

        distributionAdapter = DistributionAdapter(weeklyTipReport.individualReports)
        binding.rcyTipDistribution.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyTipDistribution.adapter = distributionAdapter

        // Button Logic
        binding.btnSaveEmployees.setOnClickListener {
            // Clearing inputted data, except for employee names
            val tipCollectionViewModel: TipCollectionViewModel by activityViewModels()
            tipCollectionViewModel.clearBillsList()
            (context as MainActivity).logWeeklyTipReport()
            findNavController().navigate(R.id.action_outputTipsFragment_to_EmployeeFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRoundingErrorDialog() {
        binding.includeRoundingErrorsDialog.root.visibility = View.VISIBLE

        val roundingError = (context as MainActivity).getWeeklyTipReport().majorRoundingError!!
        val errorMessage = if (roundingError < 0.0) {
            "You will have $${roundingError.absoluteValue} leftover."
        }
        else {
            "You will need to redistribute $${roundingError.absoluteValue}."
        }
        binding.includeRoundingErrorsDialog.tvDialogRoundingErrorMessage.text = errorMessage

        binding.includeRoundingErrorsDialog.btnDialogRoundingErrorConfirm.setOnClickListener {
            binding.includeRoundingErrorsDialog.root.visibility = View.GONE
        }
    }
}