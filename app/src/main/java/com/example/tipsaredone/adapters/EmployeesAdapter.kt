package com.example.tipsaredone.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.Employee

class EmployeesAdapter(private val itemClickCallback: ((Int) -> Unit)?) : RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder>() {

    private var employees: MutableList<Employee> = mutableListOf()

    class EmployeesViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val employeeImage: ImageView = itemView.findViewById(R.id.employee_image)
        val employeeName: TextView = itemView.findViewById(R.id.tv_employee_name)
        val employeeItem: ConstraintLayout = itemView.findViewById(R.id.cnst_employee)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee, parent, false)

        return EmployeesViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {
        holder.employeeName.text = employees[position].name
        holder.employeeItem.setOnClickListener {
            itemClickCallback?.invoke(position)
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
}