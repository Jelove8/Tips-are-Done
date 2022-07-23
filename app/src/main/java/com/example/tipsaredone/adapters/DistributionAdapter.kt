package com.example.tipsaredone.adapters

import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.Employee
import java.math.BigDecimal
import java.math.RoundingMode

class DistributionAdapter(private val employees: MutableList<Employee>) : RecyclerView.Adapter<DistributionAdapter.EmployeesViewHolder>() {

    init {
        Log.d("InternalStorage", "Adapter initialized")
    }

    class EmployeesViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val employeeIndex: TextView = itemView.findViewById(R.id.tv_employee_index)
        val employeeName: TextView = itemView.findViewById(R.id.tv_employee_name)
        val employeeTips: EditText = itemView.findViewById(R.id.et_employee_hours)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee, parent, false)

        return EmployeesViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {
        holder.employeeIndex.text = (position + 1).toString()
        holder.employeeName.text = employees[position].name

        val tipsString = employees[position].tips.toString()
        if (tipsString.contains(".0")) {
            tipsString.removeSuffix(".0")
        }
        holder.employeeTips.setText("$  $tipsString")
        holder.employeeTips.inputType = InputType.TYPE_NULL

    }

    override fun getItemCount(): Int {
        return employees.size
    }


}