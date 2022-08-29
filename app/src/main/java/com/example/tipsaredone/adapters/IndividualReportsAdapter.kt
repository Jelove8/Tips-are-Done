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
    private var tipReports: MutableList<IndividualReport>,
    private val collectClickCallback: ((Int) -> Unit)?,
    private val uncollectClickCallback: ((Int) -> Unit)?
) : RecyclerView.Adapter<IndividualReportsAdapter.EmployeesViewHolder>() {

    class EmployeesViewHolder(ItemView: View, collectClickCallback: ((Int) -> Unit)?, uncollectClickCallback: ((Int) -> Unit)?
    ) : RecyclerView.ViewHolder(ItemView) {
        private val startDate: TextView = itemView.findViewById(R.id.tv_indiv_report_startDate)
        private val endDate: TextView = itemView.findViewById(R.id.tv_indiv_report_endDate)
        private val amount: TextView = itemView.findViewById(R.id.tv_indiv_report_amount)
        private val collectButton: Button = itemView.findViewById(R.id.btn_indiv_report_collect)
        private val uncollectButton: TextView = itemView.findViewById(R.id.tv_indiv_report_checkmark)

        init {

            uncollectButton.setOnLongClickListener {
                uncollectClickCallback?.invoke(adapterPosition)
                true
            }
            collectButton.setOnClickListener {
                collectClickCallback?.invoke(adapterPosition)
            }

        }

        fun displayReportData(individualTipReport: IndividualReport) {

            startDate.text = individualTipReport.startDate.toString()
            endDate.text = individualTipReport.endDate.toString()
            amount.text = individualTipReport.distributedTips.toString()

            if (individualTipReport.collected!!) {
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
        return EmployeesViewHolder(view, collectClickCallback, uncollectClickCallback)
    }
    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {
        holder.displayReportData(tipReports[position])
    }
    override fun getItemCount(): Int {
        return tipReports.size
    }

    fun collectTips(position: Int) {
        tipReports[position].collected = true
        notifyDataSetChanged()
    }
    fun uncollectTips(position: Int) {
        tipReports[position].collected = false
        notifyDataSetChanged()
    }

}