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
import com.example.tipsaredone.model.WeeklyTipReport

class WeeklyReportsAdapter(private val weeklyReports: MutableList<WeeklyTipReport>,
                           private val itemClickCallback: ((Int) -> Unit)?
) : RecyclerView.Adapter<WeeklyReportsAdapter.BillsViewHolder>() {

    class BillsViewHolder(ItemView: View, adapter: WeeklyReportsAdapter, itemClickCallback: ((Int) -> Unit)?) : RecyclerView.ViewHolder(ItemView) {
        private val tvStartDate: TextView = itemView.findViewById(R.id.tv_weekly_report_start_date)
        private val tvEndDate: TextView = itemView.findViewById(R.id.tv_weekly_report_end_date)
        private val tvTotalHours: EditText = itemView.findViewById(R.id.tv_weekly_report_total_hours)
        private val tvTotalTips: TextView = itemView.findViewById(R.id.tv_weekly_report_collected_tips)
        private val tvTipRate: TextView = itemView.findViewById(R.id.tv_weekly_report_tip_rate)
        private val tvError: EditText = itemView.findViewById(R.id.tv_weekly_report_error)

        init {

        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_weekly_report, parent, false)
        return BillsViewHolder(view,this,itemClickCallback)
    }
    override fun onBindViewHolder(holder: BillsViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return weeklyReports.size
    }


}