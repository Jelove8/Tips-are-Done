package com.example.tipsaredone.adapters

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R
import com.example.tipsaredone.model.Employee

class EmployeesAdapter(
    private var employees: MutableList<Employee> = mutableListOf(),
    private val itemClickCallback: ((Int) -> Unit)?,
    private val textChangedCallback: ((Int) -> Unit)?
) : RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder>() {

    class EmployeesViewHolder(ItemView: View, itemClickCallback: ((Int) -> Unit)?, textChangedCallback: ((Int) -> Unit)?, adapter: EmployeesAdapter) : RecyclerView.ViewHolder(ItemView) {
        private val employeeIndex: TextView = itemView.findViewById(R.id.tv_employee_index)
        private val employeeName: TextView = itemView.findViewById(R.id.tv_employee_name)
        private val employeeItem: ConstraintLayout = itemView.findViewById(R.id.cnst_tip_distribution_header)
        val employeeHours: EditText = itemView.findViewById(R.id.et_employee_hours)

        init {
            employeeHours.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (employeeHours.text.isNullOrEmpty()) {
                        adapter.editEmployeeHours(adapterPosition,null)
                        textChangedCallback?.invoke(adapterPosition)
                    }
                    else {
                        adapter.editEmployeeHours(adapterPosition,s.toString().toDouble())
                        textChangedCallback?.invoke(adapterPosition)
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
            employeeItem.setOnClickListener {
                itemClickCallback?.invoke(adapterPosition)
            }
        }

        fun displayEmployeeInfo(employee: Employee, position: Int) {
            // Displaying index and name.
            val index = position + 1
            employeeIndex.text = index.toString()
            employeeName.text = employee.name

            if (employee.tippableHours == null) {
                employeeHours.text.clear()
            }
            else {
                employeeHours.setText(employee.tippableHours.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee, parent, false)
        return EmployeesViewHolder(view, itemClickCallback, textChangedCallback, this)
    }

    override fun onBindViewHolder(holder: EmployeesViewHolder, position: Int) {
        holder.displayEmployeeInfo(employees[position],position)
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
    }

    fun editEmployeeName(position: Int, newName: String) {
        employees[position].name = newName
        employees.sortBy { it.name }
        notifyDataSetChanged()
    }

    fun editEmployeeHours(position: Int, newHours: Double?) {
        employees[position].tippableHours = newHours
    }

}