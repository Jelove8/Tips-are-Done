package com.example.tipsaredone.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.activities.MainActivity
import com.example.tipsaredone.adapters.DistributionAdapter
import com.example.tipsaredone.databinding.FragmentTipDistributionBinding
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
        val weeklyReportGenerator = (context as MainActivity).getWeeklyReportGenerator()
        val unsavedWeeklyReport = weeklyReportGenerator.getWeeklyReport()

        if (unsavedWeeklyReport.error != 0) {
            showRoundingErrorDialog(unsavedWeeklyReport.error)
        }
        binding.tvTipRate.text = unsavedWeeklyReport.tipRate.toString()

        distributionAdapter = DistributionAdapter(mutableListOf())
        binding.rcyTipDistribution.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyTipDistribution.adapter = distributionAdapter

        // Button Logic
        binding.btnSaveEmployees.setOnClickListener {
            (context as MainActivity).saveNewWeeklyReport()
            findNavController().navigate(R.id.action_tipDistributionFragment_to_EmployeeListFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRoundingErrorDialog(error: Int) {
        binding.includeRoundingErrorsDialog.root.visibility = View.VISIBLE

        val errorMessage = if (error < 0) {
            "You will have $${error.absoluteValue} leftover."
        }
        else {
            "You will need to redistribute $${error.absoluteValue}."
        }
        binding.includeRoundingErrorsDialog.tvDialogRoundingErrorMessage.text = errorMessage

        binding.includeRoundingErrorsDialog.btnDialogRoundingErrorConfirm.setOnClickListener {
            binding.includeRoundingErrorsDialog.root.visibility = View.GONE
        }
    }
}