package com.example.tipsaredone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.Employee

class EmployeesAdapter(
    private var employees: MutableList<Employee> = mutableListOf(),
    private val itemClickCallback: ((Int) -> Unit)?,
) : RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder>() {

    init {
        employees.sortBy {
            it.name

        }
    }

    class EmployeesViewHolder(
        ItemView: View,
        itemClickCallback: ((Int) -> Unit)?
    ) : RecyclerView.ViewHolder(ItemView) {
        private val tvEmployeeIndex: TextView = itemView.findViewById(R.id.tv_employee_list_index)
        private val tvEmployeeName: TextView = itemView.findViewById(R.id.tv_employee_list_name)
        private val tvEmployeeItem: ConstraintLayout = itemView.findViewById(R.id.cnst_employee_list)
        private val tvEmployeeUncollectedLabel: TextView = itemView.findViewById(R.id.tv_employee_list_uncollected_label)
        private val tvEmployeeUncollectedValue: TextView = itemView.findViewById(R.id.tv_employee_list_uncollected_value)
        init {
            tvEmployeeItem.setOnClickListener {
                itemClickCallback?.invoke(adapterPosition)
            }
        }

        fun displayEmployeeInfo(employee: Employee) {
            val index = adapterPosition + 1
            tvEmployeeIndex.text = index.toString()

            tvEmployeeName.text = employee.name


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee_list, parent, false)
        return EmployeesViewHolder(view, itemClickCallback)
    }
    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {
        holder.displayEmployeeInfo(employees[position])
    }
    override fun getItemCount(): Int {
        return employees.size
    }

    fun getEmployees(): MutableList<Employee> {
        return employees
    }
    fun addNewEmployee(newEmployee: Employee) {
        employees.add(newEmployee)
        employees.sortBy { it.name }
        notifyDataSetChanged()
    }
    fun deleteEmployee(selectedEmployee: Employee) {
        employees.remove(selectedEmployee)
        notifyDataSetChanged()
    }
    fun editEmployee(editedEmployee: Employee) {
        employees.forEach {
            if (editedEmployee.id == it.id) {
                it.name = editedEmployee.name
            }
        }
        employees.sortBy { it.name }
        notifyDataSetChanged()
    }


}