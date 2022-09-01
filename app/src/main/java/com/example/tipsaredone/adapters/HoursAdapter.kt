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
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.EmployeeHours
import com.example.tipsaredone.model.IndividualReport
import kotlin.math.roundToInt

class HoursAdapter(
    private var employeeHours: MutableList<EmployeeHours> = mutableListOf(),
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

        fun displayReportData(employeeHoursItem: EmployeeHours, position: Int) {
            val index = position + 1
            tvIndex.text = index.toString()
            tvName.text = employeeHoursItem.name

            if (employeeHoursItem.hours == null) {
                etHours.text.clear()
            }
            else {
                etHours.setText(employeeHoursItem.hours.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee_hours, parent, false)
        return EmployeesViewHolder(view, textChangedCallback, this)
    }
    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {

        holder.displayReportData(employeeHours[position],position)

    }
    override fun getItemCount(): Int {
        return employeeHours.size
    }


    fun getListOfHours(): MutableList<EmployeeHours> {
        return employeeHours
    }
    fun editTippableHours(position: Int, newHours: Double) {
        employeeHours[position].hours = newHours
    }
    fun addEmployee(employee: Employee) {
        employeeHours.add(EmployeeHours(employee.id,employee.name,null))
        employeeHours.sortBy { it.name }
        notifyDataSetChanged()
    }
    fun getSumOfHours(): Double {
        var output = 0.0
        employeeHours.forEach {
            if (it.hours != null) {
                output += it.hours!!
            }
        }
        return (output * 100.0).roundToInt() / 100.0

    }

}