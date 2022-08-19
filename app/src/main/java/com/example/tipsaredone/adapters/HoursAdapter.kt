package com.example.tipsaredone.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.EmployeeHours
import com.example.tipsaredone.model.IndividualTipReport

class HoursAdapter(
    private var individualTipReports: MutableList<IndividualTipReport> = mutableListOf(),
    private val textChangedCallback: ((Int) -> Unit)?
) : RecyclerView.Adapter<HoursAdapter.EmployeesViewHolder>() {

    class EmployeesViewHolder(ItemView: View, textChangedCallback: ((Int) -> Unit)?, adapter: HoursAdapter
    ) : RecyclerView.ViewHolder(ItemView) {
        private val tvIndex: TextView = itemView.findViewById(R.id.tv_employee_index)
        private val tvName: TextView = itemView.findViewById(R.id.tv_indiv_report_endDate)
        private val etHours: EditText = itemView.findViewById(R.id.et_employee_hours)

        init {
            etHours.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (etHours.text.isNullOrEmpty()) {
                        adapter.editTippableHours(adapterPosition,0.0)
                        textChangedCallback?.invoke(adapterPosition)
                    }
                    else {
                        adapter.editTippableHours(adapterPosition,s.toString().toDouble())
                        textChangedCallback?.invoke(adapterPosition)
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
        }

        fun displayReportData(individualTipReport: IndividualTipReport, position: Int) {
            val index = position + 1
            tvIndex.text = index.toString()
            tvName.text = individualTipReport.employeeName

            if (individualTipReport.employeeHours == null) {
                etHours.text.clear()
            }
            else {
                etHours.setText(individualTipReport.employeeHours.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee_hours, parent, false)
        return EmployeesViewHolder(view, textChangedCallback, this)
    }
    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {

        holder.displayReportData(individualTipReports[position],position)

    }
    override fun getItemCount(): Int {
        return individualTipReports.size
    }


    fun editTippableHours(position: Int, newHours: Double) {
        individualTipReports[position].employeeHours = newHours
    }
    fun getSumOfHours(): Double {
        var output = 0.0
        individualTipReports.forEach {
            if (it.employeeHours != null) {
                output += it.employeeHours!!
            }
        }
        output *= 100
        return output / 100
    }
    fun checkForValidHours(): Boolean {
        var output = true
        individualTipReports.forEach {
            if (it.employeeHours == null) {
                output = false
            }
        }
        return  output
    }
}