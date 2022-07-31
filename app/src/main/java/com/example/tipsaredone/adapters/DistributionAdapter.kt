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

    class EmployeesViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        private val employeeIndex: TextView = itemView.findViewById(R.id.tv_employee_index)
        private val employeeName: TextView = itemView.findViewById(R.id.tv_employee_name)
        private val employeeTips: EditText = itemView.findViewById(R.id.et_employee_hours)

        fun displayEmployeeData(employee: Employee, position: Int) {
            employeeIndex.text = (position + 1).toString()
            employeeName.text = employee.name

            val tipsString = employee.tips.toString()
            if (tipsString.contains(".0")) {
                tipsString.removeSuffix(".0")
            }
            employeeTips.setText("$ $tipsString")
            employeeTips.inputType = InputType.TYPE_NULL
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee, parent, false)

        return EmployeesViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {
        holder.displayEmployeeData(employees[position],position)
    }

    override fun getItemCount(): Int {
        return employees.size
    }


}