package com.example.tipsaredone.adapters

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.IndividualReport

class DistributionAdapter(private val tipReports: MutableList<IndividualReport>) : RecyclerView.Adapter<DistributionAdapter.DistributedTipsViewHolder>() {

    class DistributedTipsViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        private val employeeIndex: TextView = itemView.findViewById(R.id.tv_employee_index)
        private val employeeName: TextView = itemView.findViewById(R.id.tv_indiv_report_endDate)
        private val employeeTips: EditText = itemView.findViewById(R.id.et_employee_hours)

        fun displayEmployeeData(tipReport: IndividualReport, position: Int) {
            employeeIndex.text = (position + 1).toString()
            employeeName.text = tipReport.name

            val tipsString = tipReport.tips.toString()
            if (tipsString.contains(".0")) {
                tipsString.removeSuffix(".0")
            }
            employeeTips.setText("$ $tipsString")
            employeeTips.inputType = InputType.TYPE_NULL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistributedTipsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee_hours, parent, false)
        return DistributedTipsViewHolder(view)
    }
    override fun onBindViewHolder(holder: DistributedTipsViewHolder, position: Int) {
        holder.displayEmployeeData(tipReports[position],position)
    }
    override fun getItemCount(): Int {
        return tipReports.size
    }
}