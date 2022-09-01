package com.example.tipsaredone.views

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tipsaredone.R
import com.example.tipsaredone.activities.MainActivity
import com.example.tipsaredone.adapters.WeeklyReportsAdapter
import com.example.tipsaredone.databinding.FragmentReportsBinding
import com.example.tipsaredone.viewmodels.EmployeesViewModel
import com.example.tipsaredone.viewmodels.ReportsViewModel

class WeeklyReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    private lateinit var reportsViewModel: ReportsViewModel

    private lateinit var weeklyReportsAdapter: WeeklyReportsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReportsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        reportsViewModel = ViewModelProvider(this)[ReportsViewModel::class.java]

        weeklyReportsAdapter = WeeklyReportsAdapter(reportsViewModel.weeklyReports.value!!,
            itemClickCallback = fun(position: Int) {

            })
        binding.rcyReportsFrag.layoutManager = LinearLayoutManager(context as MainActivity)
        binding.rcyReportsFrag.adapter = weeklyReportsAdapter

        (context as MainActivity).initializeWeeklyReports(weeklyReportsAdapter)
    }
    override fun onStart() {
        super.onStart()
        (context as MainActivity).displayNavbar(true)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}