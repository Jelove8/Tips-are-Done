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
    private val textChangedCallback: ((Int) -> Unit)?
) : RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder>() {

    companion object {
        const val THIS: String = "EmployeesAdapter"
    }

    init {
        Log.d("Initial","Adapter initialized.")
    }



    class EmployeesViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        private val employeeIndex: TextView = itemView.findViewById(R.id.tv_employee_index)
        private val employeeName: TextView = itemView.findViewById(R.id.tv_employee_name)
        val employeeItem: ConstraintLayout = itemView.findViewById(R.id.cnst_tip_distribution_header)
        val employeeHours: EditText = itemView.findViewById(R.id.et_employee_hours)

        fun displayEmployeeInfo(employee: Employee, position: Int) {

            // Displaying index and name.
            val index = position + 1
            employeeIndex.text = index.toString()
            employeeName.text = employee.name

            // Displaying hours within EditText.
            val empTippableHours = employee.tippableHours
            if (empTippableHours == null) {
                employeeHours.text = null
            }
            else {
                employeeHours.setText(empTippableHours.toString())
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee, parent, false)

        return EmployeesViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {

        holder.displayEmployeeInfo(employees[position],position)

        // Click to edit employee
        holder.employeeItem.setOnClickListener {
            itemClickCallback?.invoke(position)
        }

        // Editing employee hours
        holder.employeeHours.doAfterTextChanged {

            // Updating employee data within view model.
            updateEmployeeHours(holder.employeeHours.text.toString(), position)

            // Re-summing total hours
            textChangedCallback?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEmployeeAdapterData(list: MutableList<Employee>) {
        employees = list
        notifyDataSetChanged()
        Log.d("debug","adapter function")
    }

    fun deleteEmployee()

    private fun updateEmployeeHours(editedHours: String, position: Int) {
        employees[position].tippableHours =
            when {
                editedHours.isEmpty() -> {
                    null
                }
                editedHours.toDouble() == 0.0 -> {
                    0.0
                }
                else -> {
                    editedHours.toDouble()
                }
            }
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