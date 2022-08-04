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
import com.example.tipsaredone.adapters.BillsAdapter
import com.example.tipsaredone.databinding.FragmentCollectionBinding
import com.example.tipsaredone.viewmodels.CollectionViewModel

class CollectionFragment : Fragment() {

    private var _binding: FragmentCollectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var collectionViewModel: CollectionViewModel
    private lateinit var billsAdapter: BillsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCollectionBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val collectionVM: CollectionViewModel by activityViewModels()
        collectionViewModel = collectionVM
        updateSumOfBillsTV()

        // Bills RecyclerView
        billsAdapter = BillsAdapter(
            collectionViewModel.billsList.value!!,
            textChangedCallback = fun(_: Int, _: Double?) {
                updateSumOfBillsTV()
                updateConfirmButtonVisibility()
            }
        )
        binding.rcyBills.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyBills.adapter = billsAdapter

        binding.btnConfirmBills.setOnClickListener {
            // Navigates up if checkBillAmounts() returns true
            if (collectionViewModel.checkForValidInputs()) {
                (context as MainActivity).getRoughTipReport().updateBills(collectionViewModel.billsList.value!!)
            }
            else {
                val toast = collectionViewModel.getValidityString()
                (context as MainActivity).makeToastMessage(toast)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Dialog: Date Selector

    // Updates views
    private fun updateConfirmButtonVisibility() {
        val button: View = binding.btnConfirmBills
        if (collectionViewModel.checkForValidInputs()) {
            val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme)
            button.setBackgroundColor(sbGreen)
        }
        else {
            val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme)
            button.setBackgroundColor(wrmNeutral)
        }
    }
    private fun updateSumOfBillsTV() {
        binding.tvTotalTips.text = collectionViewModel.getSumOfBills().toString()
    }
}