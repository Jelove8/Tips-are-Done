package com.example.tipsaredone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.WeeklyReport
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
           val startDate = currentReport.startDate
           val startYear = (startDate[0].toString() + startDate[1].toString() + startDate[2].toString() + startDate[3].toString()).toInt()
           val startMonth = (startDate[5].toString() + startDate[6].toString()).toInt()
           val startDay = (startDate[8].toString() + startDate[9].toString()).toInt()

           val endDate = currentReport.endDate
           val endYear = (endDate[0].toString() + endDate[1].toString() + endDate[2].toString() + endDate[3].toString()).toInt()
           val endMonth = (endDate[5].toString() + endDate[6].toString()).toInt()
           val endDay = (endDate[8].toString() + endDate[9].toString()).toInt()

           val startMonthString: String = when (startMonth) {
               1 -> {
                   "January"
               }
               2 -> {
                   "February"
               }
               3 -> {
                   "March"
               }
               4 -> {
                   "April"
               }
               5 -> {
                   "May"
               }
               6 -> {
                   "June"
               }
               7 -> {
                   "July"
               }
               8 -> {
                   "August"
               }
               9 -> {
                   "September"
               }
               10 -> {
                   "October"
               }
               11 -> {
                   "November"
               }
               12 -> {
                   "December"
               }
               else -> {"Error"}
           }
           val endMonthString: String = when (endMonth) {
               1 -> {
                   "January"
               }
               2 -> {
                   "February"
               }
               3 -> {
                   "March"
               }
               4 -> {
                   "April"
               }
               5 -> {
                   "May"
               }
               6 -> {
                   "June"
               }
               7 -> {
                   "July"
               }
               8 -> {
                   "August"
               }
               9 -> {
                   "September"
               }
               10 -> {
                   "October"
               }
               11 -> {
                   "November"
               }
               12 -> {
                   "December"
               }
               else -> {"Error"}
           }

           val startDateString = "$startMonthString $startDay, $startYear"
           val endDateString = "$endMonthString $endDay, $endYear"

           tvStartDate.text = startDateString
           tvEndDate.text = endDateString

           val totalHours = currentReport.totalHours
           tvTotalHours.text = ((totalHours* 100.0).toInt() / 100.0).toString()
           tvTotalTips.text = currentReport.totalCollected.toString()

           val tipRate = currentReport.tipRate
           tvTipRate.text = ((tipRate * 100.0).toInt() / 100.0).toString()

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

        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        weeklyReports.sortByDescending {
            LocalDate.parse(it.startDate, dateTimeFormatter)
        }
        notifyDataSetChanged()
    }


}