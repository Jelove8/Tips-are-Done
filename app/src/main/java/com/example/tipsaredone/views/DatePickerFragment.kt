package com.example.tipsaredone.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.FragmentDatePickerBinding
import com.example.tipsaredone.viewmodels.DatePickerViewModel
import java.time.LocalDate
import java.util.*


class DatePickerFragment : Fragment() {

    private lateinit var datePickerViewModel: DatePickerViewModel
    private var _binding: FragmentDatePickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDatePickerBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datePickerVM: DatePickerViewModel by activityViewModels()
        datePickerViewModel = datePickerVM




    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
