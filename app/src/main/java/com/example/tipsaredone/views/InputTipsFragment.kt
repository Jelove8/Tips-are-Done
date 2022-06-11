package com.example.tipsaredone.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.FragmentInputTipsBinding
import com.example.tipsaredone.viewmodels.TipsViewModel

class InputTipsFragment : Fragment() {

    private var _binding: FragmentInputTipsBinding? = null
    private val binding get() = _binding!!

    private lateinit var tipsViewModel: TipsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputTipsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tipsVM: TipsViewModel by activityViewModels()
        tipsViewModel = tipsVM

        val listOfEditTexts = listOf(binding.etOnes,binding.etTwos,binding.etFives,binding.etTens,binding.etTwenties)

        for ((i, et) in listOfEditTexts.withIndex()) {

            val currentBillType = tipsViewModel.getBillsList()[i]
            // Initial data to display
            if (currentBillType == null || tipsVM.getBillsList()[i] == 0.0) {
                et.text.clear()
            }
            else {

                if (currentBillType.toString().endsWith(".0")) {
                    et.setText(currentBillType.toString() + "0")
                }
                else {
                    et.setText(currentBillType.toString())
                }

            }

            et.doAfterTextChanged {

                // Updating input-tips viewmodel
                if (et.text.isNullOrEmpty() || et.text.toString() == ".") {
                    tipsVM.updateBillAmount(i,0.00)
                }
                else {
                    tipsVM.updateBillAmount(i,et.text.toString().toDouble())
                }

                // Updating total tips textview
                sumInputTips()
            }
        }

        sumInputTips()

        binding.btnConfirmBills.setOnClickListener {
            // Navigates up if checkBillAmounts() returns true
            if (checkBillAmounts(listOfEditTexts)) findNavController().navigate(R.id.action_InputTipsFragment_toOutputTipsFragment)
        }

    }

    private fun checkBillAmounts(list: List<EditText>): Boolean {

        // Putting EditText inputs into a mutableListOf<Double> (where [0] = ones & [4] = twenties)
        val listOfAmounts = mutableListOf<Double>()
        for (et in list) {
            if (et.text.isNullOrEmpty()) {
                listOfAmounts.add(0.00)
            }
            else {
                listOfAmounts.add(et.text.toString().toDouble())
            }
        }

        // Value that will be checked when testing for valid inputs
        val sumOfModulos = (listOfAmounts[0] % 1.00) + (listOfAmounts[1] % 2.00) + (listOfAmounts[2] % 5.00) + (listOfAmounts[3] % 10.00) + (listOfAmounts[4] % 20.00)

        // Testing inputs for validity
        return when {
            // At least one EditText input must be filled.
            listOfAmounts.sum() == 0.0 -> {
                (context as MainActivity).makeToastMessage("No bills inputted.")
                false
            }
            // An input(s) is not divisible by their corresponding bill type.
            sumOfModulos != 0.0 -> {
                (context as MainActivity).makeToastMessage("Incorrect amount detected.")
                false
            }
            // Inputs are valid.
            sumOfModulos == 0.0 -> {
                true
            }
            // This branch should never be returned.
            else -> {
                Log.d(TipsViewModel.INPUT_TIPS, "InputTipsFragment / checkBillAmounts() / else branch returned.")
                false
            }
        }
    }

    private fun sumInputTips() {
        var sum = 0.00
        for (amt in tipsViewModel.getBillsList()) {
            if (amt != null) sum += amt
        }
        tipsViewModel.updateTotalTips(sum)
        binding.tvTotalTips.text = sum.toString()
    }

    fun clearBillInputs() {
        binding.etOnes.text.clear()
        binding.etTwos.text.clear()
        binding.etFives.text.clear()
        binding.etTens.text.clear()
        binding.etTwenties.text.clear()
        tipsViewModel.clearBillsList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}