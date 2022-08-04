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
import com.example.tipsaredone.model.WeeklyTipReport
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.CollectionViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.math.absoluteValue

class DistributionFragment : Fragment() {

    private var _binding: FragmentDistributionBinding? = null
    private val binding get() = _binding!!

    private lateinit var distributionAdapter: DistributionAdapter
    private lateinit var tipReport: WeeklyTipReport

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
        val billsViewModel: CollectionViewModel by activityViewModels()

        // Loading Screen
        (context as MainActivity).showCalculatingScreen()

        // Distributing Tips
        tipReport = (context as MainActivity).getRoughTipReport()
        tipReport.distributeTips()
        if (tipReport.majorRoundingError != null) {
            showRoundingErrorDialog()
        }
        displayTipRate()

        //  Tip Distribution RecyclerView
        /*
        val employees = (context as MainActivity).getRoughTipReport().employees
        distributionAdapter = DistributionAdapter(employees)
        binding.rcyTipDistribution.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyTipDistribution.adapter = distributionAdapter
*/
        // Button Logic
        binding.btnSaveEmployees.setOnClickListener {
            // Clearing inputted data, except for employee names
            billsViewModel.clearBillsList()
            (context as MainActivity).showTitleScreen()
            findNavController().navigate(R.id.action_outputTipsFragment_to_EmployeeFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRoundingErrorDialog() {
        binding.includeRoundingErrorsDialog.root.visibility = View.VISIBLE

        val roundingError = tipReport.majorRoundingError!!
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

    private fun displayTipRate() {
        val tipRate = tipReport.tipRate
        val roundedTipRate = BigDecimal(tipRate).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding.tvTipRate.text = "$ $roundedTipRate"
    }







}