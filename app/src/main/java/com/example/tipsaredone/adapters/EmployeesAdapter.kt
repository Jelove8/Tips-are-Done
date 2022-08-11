package com.example.tipsaredone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
        private val employeeIndex: TextView = itemView.findViewById(R.id.tv_employee_list_index)
        private val employeeName: TextView = itemView.findViewById(R.id.tv_employee_list_name)
        private val employeeItem: ConstraintLayout = itemView.findViewById(R.id.cnst_employee_list)
        init {
            employeeItem.setOnClickListener {
                itemClickCallback?.invoke(adapterPosition)
            }
        }

        fun displayEmployeeInfo(employee: Employee) {

            val index = adapterPosition + 1
            employeeIndex.text = index.toString()

            employeeName.text = employee.name

            val uncollectedTips = employee.checkForUncollectedTips()
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

    fun addNewEmployee(newEmployee: Employee) {
        employees.add(newEmployee)
        employees.sortBy { it.name }
        notifyDataSetChanged()
    }
    fun deleteEmployee(position: Int) {
        employees.removeAt(position)
        notifyDataSetChanged()
    }
    fun editEmployeeName(position: Int, newName: String) {
        employees[position].name = newName
        employees.sortBy { it.name }
        notifyDataSetChanged()
    }

}