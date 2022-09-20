package com.example.tipsaredone.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.EmployeeHours
import kotlin.math.roundToInt

class EmployeesAdapter(
    private var employees: MutableList<Employee> = mutableListOf(),
    private val itemClickCallback: ((Int) -> Unit)?,
    private val textChangedCallback: ((Int) -> Unit)?
) : RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder>() {

    init {
        employees.sortBy {
            it.name

        }
    }

    class EmployeesViewHolder(
        ItemView: View,
        itemClickCallback: ((Int) -> Unit)?,
        textChangedCallback: ((Int) -> Unit)?,
        adapter: EmployeesAdapter
    ) : RecyclerView.ViewHolder(ItemView) {
        private val tvEmployeeIndex: TextView = itemView.findViewById(R.id.tv_employee_list_index)
        private val tvEmployeeName: TextView = itemView.findViewById(R.id.tv_employee_list_name)
        private val tvEmployeeItem: ConstraintLayout = itemView.findViewById(R.id.cnst_employee_list)
        private val etEmployeeHours: EditText = itemView.findViewById(R.id.et_employee_hours2)
        init {
            tvEmployeeItem.setOnClickListener {
                itemClickCallback?.invoke(adapterPosition)
            }
            etEmployeeHours.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (etEmployeeHours.text.isNullOrEmpty()) {
                        adapter.editTippableHours(adapterPosition,0.0)
                        textChangedCallback?.invoke(adapterPosition)
                    }
                    else {
                        adapter.editTippableHours(adapterPosition,s.toString().toDouble())
                        textChangedCallback?.invoke(adapterPosition)
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
        }

        fun displayEmployeeInfo(employee: Employee) {
            val index = adapterPosition + 1
            tvEmployeeIndex.text = index.toString()
            tvEmployeeName.text = employee.name
            if (employee.hours == null) {
                etEmployeeHours.text.clear()
            }
            else {
                etEmployeeHours.setText(employee.hours.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee_list, parent, false)
        return EmployeesViewHolder(view, itemClickCallback, textChangedCallback, this)
    }
    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {
        holder.displayEmployeeInfo(employees[position])
    }
    override fun getItemCount(): Int {
        return employees.size
    }

    fun addNewEmployee(newEmployee: Employee) {
        var uuidCheck = true
        employees.forEach {
            if (newEmployee.id == it.id) {
                uuidCheck = false
            }
        }

        if (uuidCheck) {
            employees.add(newEmployee)
            employees.sortBy { it.name }
            notifyDataSetChanged()
        }
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

    fun editTippableHours(position: Int, newHours: Double) {
        employees[position].hours = newHours
    }

    fun getSumOfHours(): Double {
        var output = 0.0
        employees.forEach {
            if (it.hours != null) {
                output += it.hours!!
            }
        }
        return (output * 100.0).roundToInt() / 100.0

    }


}