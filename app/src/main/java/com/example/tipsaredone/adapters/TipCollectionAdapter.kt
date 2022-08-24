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

class TipCollectionAdapter(private val tipsCollected: MutableList<Double>,
                           private val textChangedCallback: ((Int,Double?) -> Unit)?
) : RecyclerView.Adapter<TipCollectionAdapter.BillsViewHolder>() {

    class BillsViewHolder(ItemView: View, adapter: TipCollectionAdapter, textChangedCallback: ((Int, Double?) -> Unit)?) : RecyclerView.ViewHolder(ItemView) {
        private val tvIndex: TextView = itemView.findViewById(R.id.tv_employee_index)
        private val tvBillType: TextView = itemView.findViewById(R.id.tv_indiv_report_endDate)
        private val etBillAmount: EditText = itemView.findViewById(R.id.et_employee_hours)
        private val tvDollarSign: TextView = itemView.findViewById(R.id.tv_dollar_sign)

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
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {122

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
                5 -> {"F I F T I E S"}
                6 -> {"H U N D R E D S"}
                else -> {
                    "Error"
                }
            }

            tvDollarSign.visibility = View.VISIBLE

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
            .inflate(R.layout.viewholder_employee_hours, parent, false)
        return BillsViewHolder(view,this,textChangedCallback)
    }
    override fun onBindViewHolder(holder: BillsViewHolder, position: Int) {
        holder.displayBillTitleAndAmount(tipsCollected[position])
    }

    override fun getItemCount(): Int {
        // Should always return 7
        return tipsCollected.size
    }

    private fun editBillAmount(position: Int,newAmount:Double?) {
        tipsCollected[position] = newAmount ?: 0.0
    }

}