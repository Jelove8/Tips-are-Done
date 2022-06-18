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
    private var employees: MutableList<Employee> = mutableListOf(),
    private val itemClickCallback: ((Int) -> Unit)?,
    private val textChangedCallback: ((Double) -> Unit)?
) : RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder>() {

    companion object {
        const val THIS: String = "EmployeesAdapter"
    }



    class EmployeesViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val employeeIndex: TextView = itemView.findViewById(R.id.tv_employee_index)
        val employeeName: TextView = itemView.findViewById(R.id.tv_employee_name)
        val employeeItem: ConstraintLayout = itemView.findViewById(R.id.cnst_tip_distribution_header)
        val employeeHours: EditText = itemView.findViewById(R.id.et_employee_hours)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee, parent, false)

        return EmployeesViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {

        // Displaying employee index & name.
        holder.employeeName.text = employees[position].name
        val index = position + 1
        holder.employeeIndex.text = "$index"

        // Displaying employee hours.
        val empTippableHours = employees[position].tippableHours
        if (empTippableHours == null) {
            holder.employeeHours.text = null
        }
        else {
            holder.employeeHours.setText(empTippableHours.toString())
        }

        // Click to edit employee
        holder.employeeItem.setOnClickListener {
            itemClickCallback?.invoke(position)
        }

        // Editing employee hours
        holder.employeeHours.doAfterTextChanged {
            val editedHours = holder.employeeHours.text

            // Updating employee data within view model.
            employees[position].tippableHours =
                when {
                    editedHours.isNullOrEmpty() -> {
                        null
                    }
                    editedHours.toString().toDouble() == 0.0 -> {
                        0.0
                    }
                    else -> {
                        editedHours.toString().toDouble()
                    }
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
        notifyDataSetChanged()
    }

    fun deleteEmployeeFromAdapter(position: Int) {
        notifyItemRemoved(position)
        textChangedCallback?.invoke(getSumHours())

        val empToRemove = employees[position].name
        Log.d(THIS,"Employee deleted from adapter: $empToRemove, position = $position")
    }

    private fun getSumHours(): Double {
        var output = 0.00
        for (emp in employees) {
            if (emp.tippableHours != null) {
                output += emp.tippableHours.toString().toDouble() * 100
            }
        }
        return output / 100
    }

    fun checkForNullHours(): Boolean {
        var bool = true
        for (emp in employees) {
            if (emp.tippableHours == null) {
                bool = false
                break
            }
        }
        return bool
    }
    fun checkEmployees(): Boolean {
        return employees.size > 1
    }
}