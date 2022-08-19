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
        updateSumOfBillsTV()

        // Bills RecyclerView
        tipCollectionAdapter = TipCollectionAdapter(
            tipCollectionViewModel.tipsCollected.value!!,
            textChangedCallback = fun(_: Int, _: Double?) {
                updateSumOfBillsTV()
                updateConfirmButtonVisibility()
            }
        )
        binding.rcyTipCollection.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyTipCollection.adapter = tipCollectionAdapter

        binding.btnTipCollectionConfirm.setOnClickListener {
            // Navigates up if checkBillAmounts() returns true
            if (checkForValidInputs()) {
                val tipsCollected = tipCollectionViewModel.tipsCollected.value!!
                (context as MainActivity).getWeeklyTipReport().setTipsCollected(tipsCollected)
                (context as MainActivity).showCalculatingScreen()
                findNavController().navigate(R.id.action_CollectionFrag_to_DistributionFrag)
            }
            else {
                val toast = getValidityString()
                (context as MainActivity).makeToastMessage(toast)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Updates views
    private fun updateConfirmButtonVisibility() {
        val button: View = binding.btnTipCollectionConfirm
        if (checkForValidInputs()) {
            val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme)
            button.setBackgroundColor(sbGreen)
        }
        else {
            val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme)
            button.setBackgroundColor(wrmNeutral)
        }
    }
    private fun updateSumOfBillsTV() {
        binding.tvTotalTips.text = tipCollectionViewModel.getTotalCollected().toString()
    }

    private fun checkForValidInputs(): Boolean {
        val sumOfModulos = (tipCollectionViewModel.tipsCollected.value!![0] % 1.00) +
                (tipCollectionViewModel.tipsCollected.value!![1] % 2.00) +
                (tipCollectionViewModel.tipsCollected.value!![2] % 5.00) +
                (tipCollectionViewModel.tipsCollected.value!![3] % 10.00) +
                (tipCollectionViewModel.tipsCollected.value!![4] % 20.00) +
                (tipCollectionViewModel.tipsCollected.value!![5] % 50.00) +
                (tipCollectionViewModel.tipsCollected.value!![6] % 100.00)
        return sumOfModulos == 0.0
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
                resources.getString(R.string.invalid_bills1)
            }
            // An input(s) is not divisible by their corresponding bill type.
            sumOfModulos != 0.0 -> {
                resources.getString(R.string.invalid_bills2)
            }
            else -> {
                resources.getString(R.string.error_has_occurred)
            }
        }

    }
}