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

    class BillsViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
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
                   "Jan"
               }
               2 -> {
                   "Feb"
               }
               3 -> {
                   "Mar"
               }
               4 -> {
                   "Apr"
               }
               5 -> {
                   "May"
               }
               6 -> {
                   "Jun"
               }
               7 -> {
                   "Jul"
               }
               8 -> {
                   "Aug"
               }
               9 -> {
                   "Sep"
               }
               10 -> {
                   "Oct"
               }
               11 -> {
                   "Nov"
               }
               12 -> {
                   "Dec"
               }
               else -> {"Error"}
           }
           val endMonthString: String = when (endMonth) {
               1 -> {
                   "Jan"
               }
               2 -> {
                   "Feb"
               }
               3 -> {
                   "Mar"
               }
               4 -> {
                   "Apr"
               }
               5 -> {
                   "May"
               }
               6 -> {
                   "Jun"
               }
               7 -> {
                   "Jul"
               }
               8 -> {
                   "Aug"
               }
               9 -> {
                   "Sep"
               }
               10 -> {
                   "Oct"
               }
               11 -> {
                   "Nov"
               }
               12 -> {
                   "Dec"
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
        return BillsViewHolder(view)
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