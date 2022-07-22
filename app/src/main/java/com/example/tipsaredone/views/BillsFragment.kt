package com.example.tipsaredone.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.adapters.BillsAdapter
import com.example.tipsaredone.databinding.FragmentInputTipsBinding
import com.example.tipsaredone.viewmodels.BillsViewModel

class BillsFragment : Fragment() {

    private var _binding: FragmentInputTipsBinding? = null
    private val binding get() = _binding!!

    private lateinit var billsViewModel: BillsViewModel
    private lateinit var billsAdapter: BillsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputTipsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val billsVM: BillsViewModel by activityViewModels()
        billsViewModel = billsVM

        billsAdapter = BillsAdapter(
            billsViewModel.billsList.value!!,
            textChangedCallback = fun(_: Int, _: Double?) {
                updateSumOfBills()
                checkForValidInputs()
            }
        )
        binding.rcyBills.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyBills.adapter = billsAdapter

        updateSumOfBills()


        binding.btnConfirmBills.setOnClickListener {
            // Navigates up if checkBillAmounts() returns true
            if (billsViewModel.getConfirmButtonBool()) {
                findNavController().navigate(R.id.action_InputTipsFragment_toOutputTipsFragment)
            }
            else {
                val toast = billsViewModel.checkForValidAmounts()
                (context as MainActivity).makeToastMessage(toast)
            }

        }

    }

    private fun updateSumOfBills() {
        binding.tvTotalTips.text = billsViewModel.getSumOfBills().toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkForValidInputs() {     // Checks if user should be able to click the Confirm button.
        billsViewModel.checkForValidAmounts()
        displayConfirmButton()
    }
    private fun displayConfirmButton() {
        // https://stackoverflow.com/questions/23517879/set-background-color-programmatically
        val button: View = binding.btnConfirmBills

        if (billsViewModel.getConfirmButtonBool()) {     // Button is clickable and visible.
            val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme)
            button.setBackgroundColor(sbGreen)
        }
        else {  // Button is not clickable and is hidden.
            val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme)
            button.setBackgroundColor(wrmNeutral)
        }
    }

}