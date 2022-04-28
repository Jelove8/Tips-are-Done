package com.example.tipsaredone.adapters

import android.annotation.SuppressLint
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

class EmployeeHoursAdapter(private val itemClickCallback: ((Int,Double) -> Unit)?) : RecyclerView.Adapter<EmployeeHoursAdapter.EmployeeHoursViewHolder>() {

    private var employees: MutableList<Employee> = mutableListOf()
    private var hours: MutableList<Double> = mutableListOf()

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
        holder.etHours.setText(employees[position].currentTippableHours.toString())
        holder.etHours.doAfterTextChanged {
            itemClickCallback?.invoke(position,holder.etHours.text.toString().toDouble())
        }
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEmployeeAdapterData(newEmployees: MutableList<Employee>, newHours: MutableList<Double>) {
        this.employees = newEmployees
        this.hours = newHours
        notifyDataSetChanged()
    }
}