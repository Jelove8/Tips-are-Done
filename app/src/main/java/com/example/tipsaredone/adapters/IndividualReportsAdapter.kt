package com.example.tipsaredone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.IndividualReport

class IndividualReportsAdapter(
    private var individualReports: MutableList<IndividualReport>,
    private val collectClickCallback: ((String) -> Unit)?,
    private val uncollectClickCallback: ((String) -> Unit)?
) : RecyclerView.Adapter<IndividualReportsAdapter.EmployeesViewHolder>() {

    class EmployeesViewHolder(ItemView: View, adapter: IndividualReportsAdapter, collectClickCallback: ((String) -> Unit)?, uncollectClickCallback: ((String) -> Unit)?
    ) : RecyclerView.ViewHolder(ItemView) {
        private val startDate: TextView = itemView.findViewById(R.id.tv_indiv_report_startDate)
        private val endDate: TextView = itemView.findViewById(R.id.tv_indiv_report_endDate)
        private val amount: TextView = itemView.findViewById(R.id.tv_indiv_report_amount)
        private val tvHours: TextView = itemView.findViewById(R.id.tv_indiv_report_hours)
        private val collectButton: Button = itemView.findViewById(R.id.btn_indiv_report_collect)
        private val uncollectButton: TextView = itemView.findViewById(R.id.tv_indiv_report_checkmark)

        init {

            uncollectButton.setOnLongClickListener {
                uncollectClickCallback?.invoke(adapter.individualReports[adapterPosition].reportID)
                true
            }
            collectButton.setOnClickListener {
                collectClickCallback?.invoke(adapter.individualReports[adapterPosition].reportID)
            }

        }

        fun displayReportData(individualTipReport: IndividualReport) {

            startDate.text = individualTipReport.startDate
            endDate.text = individualTipReport.endDate
            amount.text = individualTipReport.tips.toString()
            tvHours.text = individualTipReport.hours.toString()

            if (individualTipReport.collected) {
                collectButton.visibility = View.GONE
                uncollectButton.visibility = View.VISIBLE
            }
            else {
                collectButton.visibility = View.VISIBLE
                uncollectButton.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_individual_reports, parent, false)
        return EmployeesViewHolder(view, this, collectClickCallback, uncollectClickCallback)
    }
    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {
        holder.displayReportData(individualReports[position])
    }
    override fun getItemCount(): Int {
        return individualReports.size
    }

    fun collectTips(selectedReportID: String, isCollecting: Boolean) {
        var position = 0
        individualReports.forEach {
            if (it.reportID == selectedReportID) {
                it.collected = isCollecting
                position = individualReports.indexOf(it)
            }
        }
        notifyItemChanged(position)
    }


}