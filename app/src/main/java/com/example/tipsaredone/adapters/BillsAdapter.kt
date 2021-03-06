package com.example.tipsaredone.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tipsaredone.R

class BillsAdapter(private val bills: MutableList<Double>,
                   private val textChangedCallback: ((Int,Double?) -> Unit)?
) : RecyclerView.Adapter<BillsAdapter.BillsViewHolder>() {

    class BillsViewHolder(ItemView: View, adapter: BillsAdapter, textChangedCallback: ((Int, Double?) -> Unit)?) : RecyclerView.ViewHolder(ItemView) {
        private val tvIndex: TextView = itemView.findViewById(R.id.tv_employee_index)
        private val tvBillType: TextView = itemView.findViewById(R.id.tv_employee_name)
        private val etBillAmount: EditText = itemView.findViewById(R.id.et_employee_hours)

        init {
            tvIndex.visibility = View.INVISIBLE

            etBillAmount.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (etBillAmount.text.isNullOrEmpty()) {
                        adapter.editBillAmount(adapterPosition,null)
                        textChangedCallback?.invoke(adapterPosition,null)
                    }
                    else {
                        adapter.editBillAmount(adapterPosition,s.toString().toDouble())
                        textChangedCallback?.invoke(adapterPosition,s.toString().toDouble())
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
        }

        fun displayBillTitleAndAmount(amount: Double) {

            tvBillType.text = when (adapterPosition) {
                0 -> {"O N E S"}
                1 -> {"T W O S"}
                2 -> {"F I V E S"}
                3 -> {"T E N S"}
                4 -> {"T W E N T I E S"}
                else -> {
                    "Error"
                }
            }

            if (amount == 0.0) {
                etBillAmount.text.clear()
            }
            else {
                etBillAmount.setText(amount.toString())
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_employee, parent, false)

        return BillsViewHolder(view,this,textChangedCallback)
    }

    override fun onBindViewHolder(holder: BillsViewHolder, position: Int) {
        holder.displayBillTitleAndAmount(bills[position])
    }

    override fun getItemCount(): Int {
        // Should always return 5
        return bills.size
    }

    private fun editBillAmount(position: Int,newAmount:Double?) {
        bills[position] = newAmount ?: 0.0
    }

}