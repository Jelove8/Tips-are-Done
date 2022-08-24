package com.example.tipsaredone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.WeeklyReport

class WeeklyReportsAdapter(private val weeklyReports: MutableList<WeeklyReport>,
                           private val itemClickCallback: ((Int) -> Unit)?
) : RecyclerView.Adapter<WeeklyReportsAdapter.BillsViewHolder>() {

    class BillsViewHolder(ItemView: View, adapter: WeeklyReportsAdapter, itemClickCallback: ((Int) -> Unit)?) : RecyclerView.ViewHolder(ItemView) {
        private val tvStartDate: TextView = itemView.findViewById(R.id.tv_weekly_report_start_date)
        private val tvEndDate: TextView = itemView.findViewById(R.id.tv_weekly_report_end_date)
        private val tvTotalHours: TextView = itemView.findViewById(R.id.tv_weekly_report_total_hours)
        private val tvTotalTips: TextView = itemView.findViewById(R.id.tv_weekly_report_collected_tips)
        private val tvTipRate: TextView = itemView.findViewById(R.id.tv_weekly_report_tip_rate)
        private val tvError: TextView = itemView.findViewById(R.id.tv_weekly_report_error)

       fun displayWeeklyReport(currentReport: WeeklyReport) {
           tvStartDate.text = currentReport.startDate
           tvEndDate.text = currentReport.endDate

           tvTotalHours.text = currentReport.totalHours.toString()
           tvTotalTips.text = currentReport.totalCollected.toString()
           tvTipRate.text = currentReport.tipRate.toString()

           val error = currentReport.majorRoundingError
           tvError.text =
               if (error == 0) { "No Error" }
               else { "$$error" }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_weekly_report, parent, false)
        return BillsViewHolder(view,this,itemClickCallback)
    }
    override fun onBindViewHolder(holder: BillsViewHolder, position: Int) {
        holder.displayWeeklyReport(weeklyReports[position])
    }

    override fun getItemCount(): Int {
        return weeklyReports.size
    }

    fun addNewWeeklyReport(weeklyReport: WeeklyReport) {
        weeklyReports.add(weeklyReport)
        weeklyReports.sortBy { it.endDate }
        notifyDataSetChanged()
    }


}