package com.example.tipsaredone.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.Employee

class EmployeesAdapter(
    private val itemClickCallback: ((Int) -> Unit)?,
    private val textChangedCallback: ((Double) -> Unit)?
) : RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder>() {

    private var employees: MutableList<Employee> = mutableListOf()

    class EmployeesViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val employeeIndex: TextView = itemView.findViewById(R.id.tv_employee_index)
        val employeeName: TextView = itemView.findViewById(R.id.tv_employee_name)
        val employeeItem: ConstraintLayout = itemView.findViewById(R.id.cnst_employee_list_header)
        val employeeHours: EditText = itemView.findViewById(R.id.et_employee_hours)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee, parent, false)

        return EmployeesViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {

        // Display employee data
        holder.employeeIndex.text = (position + 1).toString()
        holder.employeeName.text = employees[position].name
        holder.employeeHours.setText(employees[position].currentTippableHours)

        // Click to edit employee
        holder.employeeItem.setOnClickListener {
            itemClickCallback?.invoke(position)
        }

        // Editing employee hours
        holder.employeeHours.doAfterTextChanged {
            val editedHours = holder.employeeHours.text
            employees[position].currentTippableHours =
                if (editedHours.isNullOrEmpty()) {
                    null
                }
                else {

                    editedHours.toString()
                }

            // Re-summing total hours
            textChangedCallback?.invoke(getSumHours())

        }
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEmployeeAdapterData(list: MutableList<Employee>) {
        employees = list
        employees.sortBy { it.name }
        notifyDataSetChanged()
    }
    private fun getSumHours(): Double {
        var output = 0.00
        for (emp in employees) {
            if (!emp.currentTippableHours.isNullOrEmpty()) {
                output += emp.currentTippableHours.toString().toDouble() * 100
            }
        }
        return output / 100
    }
}