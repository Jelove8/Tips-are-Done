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
    private val textChangedCallback: ((Int,Double?) -> Unit)?
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
            if (empTippableHours == null || empTippableHours == 0.00) {
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

            Log.d("EmployeeList","ECHANGE!!!!!")

        }
    }

    override fun getItemCount(): Int {
        return employees.size
    }




    fun addNewEmployee(newEmployee: Employee) {
        employees.add(newEmployee)
        employees.sortBy { it.name }
        notifyDataSetChanged()
        Log.d("EmployeeList","New employee added: ${newEmployee.name}")
    }

    fun deleteEmployee(position: Int) {
        employees.removeAt(position)
        notifyDataSetChanged()
        Log.d("EmployeeList","Employee deleted: ${employees[position].name}")
    }

    fun editEmployeeName(position: Int, newName: String) {
        employees[position].name = newName
        employees.sortBy { it.name }
        notifyDataSetChanged()
        Log.d("EmployeeList","Employee name changed @ [$position]: ${employees[position].name}")
    }

    fun editEmployeeHours(position: Int, newHours: Double?) {
        employees[position].tippableHours = newHours
        notifyDataSetChanged()
        Log.d("EmployeeList","Employee hours changed @ [$position]: ${employees[position].tippableHours}")
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