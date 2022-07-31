package com.example.tipsaredone.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.DistributionAdapter
import com.example.tipsaredone.databinding.FragmentDistributionBinding
import com.example.tipsaredone.model.TipReport
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.BillsViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.absoluteValue

class DistributionFragment : Fragment() {

    private var _binding: FragmentDistributionBinding? = null
    private val binding get() = _binding!!

    private lateinit var distributionAdapter: DistributionAdapter
    private lateinit var tipReport: TipReport

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
        val billsViewModel: BillsViewModel by activityViewModels()

        // Loading Screen
        (context as MainActivity).hideToolbar()
        Timer().schedule(1000){
            (context as MainActivity).runOnUiThread {
                (context as MainActivity).showToolbar()
                hideLoadingScreen()
            }
        }

        // Calculating Tips
        tipReport = TipReport(employeesViewModel.employees.value!!,billsViewModel.billsList.value!!)
        val totalHours = employeesViewModel.getSumHours()
        val totalBills = billsViewModel.getSumOfBills()
        val roundingError = tipReport.distributeTips(totalHours,totalBills)
        checkRoundingError(roundingError)
        displayTipRate()

        //  Tip Distribution RecyclerView
        distributionAdapter = DistributionAdapter(employeesViewModel.employees.value!!)
        binding.rcyTipDistribution.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyTipDistribution.adapter = distributionAdapter

        // Button Logic
        binding.includeRoundingErrorsDialog.btnDialogConfirm.setOnClickListener {
            binding.includeRoundingErrorsDialog.root.visibility = View.GONE
        }

        binding.btnSaveEmployees.setOnClickListener {
            // Clearing inputted data, except for employee names
            employeesViewModel.clearEmployeeHoursAndDistributedTips()
            billsViewModel.clearBillsList()
            (context as MainActivity).showTitleScreen(true)
            findNavController().navigate(R.id.action_outputTipsFragment_to_EmployeeFragment)
        }
    }

    private fun displayTipRate() {
        val tipRate = tipReport.getTipRate()
        val roundedTipRate = BigDecimal(tipRate).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding.tvTipRate.text = roundedTipRate
    }

    private fun checkRoundingError(roundingError: Double?) {
        val dialogBox = binding.includeRoundingErrorsDialog.root
        val dialogTextView: TextView = dialogBox.findViewById(R.id.et_dialog_box)

        if (roundingError == null) {
            dialogBox.visibility = View.GONE
        }
        else if (roundingError > 0) {
            dialogTextView.text = "You will be short ${roundingError.absoluteValue.toInt()} dollars"
        }
        else if (roundingError < 0) {
            dialogTextView.text = "You will have ${roundingError.absoluteValue.toInt()} dollars remaining."
        }
    }


    private fun hideLoadingScreen() {
        binding.loadingScreen.root.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}