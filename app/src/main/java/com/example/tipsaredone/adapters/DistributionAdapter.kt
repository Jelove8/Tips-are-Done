package com.example.tipsaredone.adapters

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.Employee
import java.math.BigDecimal
import java.math.RoundingMode

class DistributionAdapter(private val employees: MutableList<Employee>) : RecyclerView.Adapter<DistributionAdapter.EmployeesViewHolder>() {

    class EmployeesViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val employeeIndex: TextView = itemView.findViewById(R.id.tv_employee_index)
        val employeeName: TextView = itemView.findViewById(R.id.tv_employee_name)
        val employeeTips: EditText = itemView.findViewById(R.id.et_employee_hours)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee, parent, false)

        return DistributionAdapter.EmployeesViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {
        holder.employeeIndex.text = (position + 1).toString()
        holder.employeeName.text = employees[position].name

        val distributedTips = employees[position].distributedTips.toInt()

        holder.employeeTips.setText("$  $distributedTips.00")
        holder.employeeTips.inputType = InputType.TYPE_NULL

    }

    override fun getItemCount(): Int {
        return employees.size
    }


}