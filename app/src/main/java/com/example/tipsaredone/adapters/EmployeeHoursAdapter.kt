package com.example.tipsaredone.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.Employee
import org.w3c.dom.Text

class EmployeeHoursAdapter(private val itemClickCallback: (Editable, String) -> Unit) : RecyclerView.Adapter<EmployeeHoursAdapter.EmployeeHoursViewHolder>() {

    private var employees: MutableList<Employee> = mutableListOf()
    private var hoursMap: MutableMap<String,Double?> = mutableMapOf()

    class EmployeeHoursViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvIndex: TextView = itemView.findViewById(R.id.tv_empoyee_index)
        val tvName: TextView = itemView.findViewById(R.id.tv_employee_hours)
        val etHours: EditText = itemView.findViewById(R.id.et_employee_hours)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeHoursViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee_hours, parent, false)
        return EmployeeHoursViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EmployeeHoursViewHolder,
        position: Int
    ) {
        holder.tvIndex.text ="${position+1}."
        holder.tvName.text = employees[position].name

        if (hoursMap[employees[position].id] == null || hoursMap[employees[position].id] == 0.00) {
            holder.etHours.text.clear()
        }
        else {
            holder.etHours.setText(hoursMap[employees[position].id].toString())
        }

        holder.etHours.doAfterTextChanged {
            itemClickCallback.invoke(holder.etHours.text,employees[position].id)
        }
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEmployeeAdapterData(newEmployees: MutableList<Employee>, newHours: MutableMap<String,Double?>) {
        this.employees = newEmployees
        this.hoursMap = newHours
        notifyDataSetChanged()
    }
}