package com.example.tipsaredone.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tipsaredone.databinding.FragmentInputTipsBinding
import com.example.tipsaredone.viewmodels.TipsViewModel

class InputTipsFragment : Fragment() {

    private var _binding: FragmentInputTipsBinding? = null
    private val binding get() = _binding!!

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

        val listOfEditTexts = listOf(binding.etOnes,binding.etTwos,binding.etFives,binding.etTens,binding.etTwenties)

        for ((i, et) in listOfEditTexts.withIndex()) {

            // Initial data to display
            if (tipsVM.getBillsList()[i] == null || tipsVM.getBillsList()[i] == 0) {
                et.text.clear()
            }
            else {
                et.setText(tipsVM.getBillsList()[i].toString())
            }

            et.doAfterTextChanged {
                if (et.text.isNullOrEmpty()) {
                    tipsVM.updateBillAmount(i,null)
                }
                else {
                    tipsVM.updateBillAmount(i,et.text.toString().toInt())
                }

            }
        }

        binding.btnConfirmBills.setOnClickListener {
            checkBillAmounts(listOfEditTexts)
        }

    }


    private fun checkBillAmounts(list: List<EditText>) {
        val listOfAmounts = mutableListOf<Int>()
        for (et in list) {
            if (et.text.isNullOrEmpty()) {
                listOfAmounts.add(0)
            }
            else {
                listOfAmounts.add(et.text.toString().toInt())
            }
        }

        val sumOfMods = (listOfAmounts[1] % 2) + (listOfAmounts[2] % 5) + (listOfAmounts[3] % 10) + (listOfAmounts[4] % 20)

        when {
            listOfAmounts.sum() == 0 -> {
                (context as MainActivity).makeToastMessage("No bills inputted.")
            }
            sumOfMods != 0 -> {
                (context as MainActivity).makeToastMessage("Incorrect amount detected.")
            }
            else -> {
                (context as MainActivity).makeToastMessage("Bills confirmed")
            }
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}