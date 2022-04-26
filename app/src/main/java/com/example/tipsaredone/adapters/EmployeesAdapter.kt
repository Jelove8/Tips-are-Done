package com.example.tipsaredone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.Employee

class EmployeesAdapter(
    private val employees: MutableList<Employee>
) : RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder>() {

    class EmployeesViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val employeeImage: ImageView = itemView.findViewById(R.id.employee_image)
        val employeeName: TextView = itemView.findViewById(R.id.employee_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee, parent, false)

        return EmployeesViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {
        holder.employeeName.text = employees[position].name
    }

    override fun getItemCount(): Int {
        return employees.size
    }
}