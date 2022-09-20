package com.example.tipsaredone.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.activities.MainActivity
import com.example.tipsaredone.adapters.TipCollectionAdapter
import com.example.tipsaredone.databinding.FragmentTipCollectionBinding
import com.example.tipsaredone.viewmodels.TipCollectionViewModel


class TipCollectionFragment : Fragment() {

    private var _binding: FragmentTipCollectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var tipCollectionViewModel: TipCollectionViewModel
    private lateinit var tipCollectionAdapter: TipCollectionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTipCollectionBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tipCollectionVM: TipCollectionViewModel by activityViewModels()
        tipCollectionViewModel = tipCollectionVM

        /**
         * TEXT CHANGE CALLBACK:    Updates sum of inputted amounts displayed
         */
        tipCollectionAdapter = TipCollectionAdapter(tipCollectionViewModel.tipsCollected.value!!,
            textChangedCallback = fun(_: Double?) {
                updateSumOfBillsTV()
                updateConfirmButtonVisibility()
            }
        )
        binding.rcyTipCollection.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyTipCollection.adapter = tipCollectionAdapter
        updateSumOfBillsTV()

        binding.btnConfirmCollection.setOnClickListener {
            if (checkForValidInputs()) {
                val collectedTips = tipCollectionViewModel.tipsCollected.value!!
                (context as MainActivity).collectWeeklyTips(collectedTips)
                (context as MainActivity).displayCalculatingScreen()
                findNavController().navigate(R.id.action_tipCollectionFragment_to_tipDistributionFragment)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        (context as MainActivity).displayNavbar(false)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Misc
    private fun updateSumOfBillsTV() {
    }
    private fun updateConfirmButtonVisibility() {
        val sumOfModulos = (tipCollectionViewModel.tipsCollected.value!![0] % 1.00) +
                (tipCollectionViewModel.tipsCollected.value!![1] % 2.00) +
                (tipCollectionViewModel.tipsCollected.value!![2] % 5.00) +
                (tipCollectionViewModel.tipsCollected.value!![3] % 10.00) +
                (tipCollectionViewModel.tipsCollected.value!![4] % 20.00) +
                (tipCollectionViewModel.tipsCollected.value!![5] % 50.00) +
                (tipCollectionViewModel.tipsCollected.value!![6] % 100.00)

        if (sumOfModulos == 0.0) {
            val sbGreen = ResourcesCompat.getColor(resources,R.color.starbucks_green,(context as MainActivity).theme)
            binding.btnConfirmCollection.setBackgroundColor(sbGreen)
        } else {
            val wrmNeutral = ResourcesCompat.getColor(resources,R.color.warm_neutral,(context as MainActivity).theme)
            binding.btnConfirmCollection.setBackgroundColor(wrmNeutral)
        }
    }
    private fun checkForValidInputs(): Boolean {
        val sumOfModulos = (tipCollectionViewModel.tipsCollected.value!![0] % 1.00) +
                (tipCollectionViewModel.tipsCollected.value!![1] % 2.00) +
                (tipCollectionViewModel.tipsCollected.value!![2] % 5.00) +
                (tipCollectionViewModel.tipsCollected.value!![3] % 10.00) +
                (tipCollectionViewModel.tipsCollected.value!![4] % 20.00) +
                (tipCollectionViewModel.tipsCollected.value!![5] % 50.00) +
                (tipCollectionViewModel.tipsCollected.value!![6] % 100.00)

        return if (sumOfModulos == 0.0) {
            true
        }
        else {
            (context as MainActivity).makeToastMessage(getValidityString())
            false
        }
    }
    private fun getValidityString(): String {
        val sumOfModulos = (tipCollectionViewModel.tipsCollected.value!![0] % 1.00) +
                (tipCollectionViewModel.tipsCollected.value!![1] % 2.00) +
                (tipCollectionViewModel.tipsCollected.value!![2] % 5.00) +
                (tipCollectionViewModel.tipsCollected.value!![3] % 10.00) +
                (tipCollectionViewModel.tipsCollected.value!![4] % 20.00) +
                (tipCollectionViewModel.tipsCollected.value!![5] % 50.00) +
                (tipCollectionViewModel.tipsCollected.value!![6] % 100.00)

        return when {
            // At least one EditText input must be filled.
            tipCollectionViewModel.tipsCollected.value!!.sum() == 0.0 -> {
                resources.getString(com.example.tipsaredone.R.string.invalid_bills1)
            }
            // An input(s) is not divisible by their corresponding bill type.
            sumOfModulos != 0.0 -> {
                resources.getString(com.example.tipsaredone.R.string.invalid_bills2)
            }
            else -> {
                resources.getString(com.example.tipsaredone.R.string.error_has_occurred)
            }
        }

    }

}