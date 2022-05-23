package com.example.tipsaredone.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tipsaredone.databinding.FragmentNumpadBinding

class NumpadFragment(private val parentFrag: EmployeeListFragment) : Fragment() {

    private var _binding: FragmentNumpadBinding? = null
    private val binding get() = _binding!!
    private var employeeIndex: Int? = null
    private var output: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNumpadBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOne.setOnClickListener {
            output += "1"
        }
        binding.btnTwo.setOnClickListener {
            output += "2"
        }
        binding.btnThree.setOnClickListener {
            output += "3"
        }
        binding.btnFour.setOnClickListener {
            output += "4"
        }
        binding.btnFive.setOnClickListener {
            output += "5"
        }
        binding.btnSix.setOnClickListener {
            output += "6"
        }
        binding.btnSeven.setOnClickListener {
            output += "7"
        }
        binding.btnEight.setOnClickListener {
            output += "8"
        }
        binding.btnNine.setOnClickListener {
            output += "9"
        }
        binding.btnZero.setOnClickListener {
            output += "0"
        }
        binding.btnPeriod.setOnClickListener {
            if (!output.isNullOrEmpty() && !output!!.contains(".")) {
                output += "."
            }
        }

        binding.btnBackspace.setOnClickListener {

        }
        binding.btnConfirmNumpad.setOnClickListener {

        }
        binding.btnCancelNumpad.setOnClickListener {

            Log.d("Meow", "Cancel pressed")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun selectIndex(index: Int) {
        employeeIndex = index
    }

}