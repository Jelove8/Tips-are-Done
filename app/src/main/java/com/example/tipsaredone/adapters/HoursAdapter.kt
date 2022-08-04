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
import com.example.tipsaredone.model.IndividualTipReport

class HoursAdapter(
    private var tipReports: MutableList<IndividualTipReport> = mutableListOf(),
    private val textChangedCallback: ((Int) -> Unit)?
) : RecyclerView.Adapter<HoursAdapter.EmployeesViewHolder>() {

    class EmployeesViewHolder(ItemView: View, textChangedCallback: ((Int) -> Unit)?, adapter: HoursAdapter
    ) : RecyclerView.ViewHolder(ItemView) {
        private val employeeIndex: TextView = itemView.findViewById(R.id.tv_employee_index)
        private val employeeName: TextView = itemView.findViewById(R.id.tv_indiv_report_endDate)
        private val employeeHours: EditText = itemView.findViewById(R.id.et_employee_hours)

        init {
            employeeHours.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (employeeHours.text.isNullOrEmpty()) {
                        adapter.editTippableHours(adapterPosition,null)
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

        fun displayEmployeeInfo(tipReport: IndividualTipReport, position: Int) {
            val index = position + 1
            employeeIndex.text = index.toString()
            employeeName.text = tipReport.employeeName

            if (tipReport.employeeHours == null) {
                employeeHours.text.clear()
            }
            else {
                employeeHours.setText(tipReport.employeeHours.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee_hours, parent, false)
        return EmployeesViewHolder(view, textChangedCallback, this)
    }
    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {
        holder.displayEmployeeInfo(tipReports[position],position)
    }
    override fun getItemCount(): Int {
        return tipReports.size
    }

    fun editTippableHours(position: Int, newHours: Double?) {
        tipReports[position].employeeHours = newHours
    }
}