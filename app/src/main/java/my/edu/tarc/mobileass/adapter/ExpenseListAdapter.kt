package my.edu.tarc.mobileass.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import my.edu.tarc.mobileass.databinding.AllExpenseLayoutBinding
import my.edu.tarc.mobileass.model.ExpenseViewModel
class ExpenseListAdapter(private val list:ArrayList<ExpenseViewModel>, val context : Context):RecyclerView.Adapter<ExpenseListAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding:AllExpenseLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = AllExpenseLayoutBinding.inflate(LayoutInflater.from(context),parent ,false)
        return  MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data: ExpenseViewModel = list[position]
        holder.binding.textDate.text=data.date
        holder.binding.texttotalExpense.text="RM"+data.expense
        holder.binding.textTitle.text=data.title
        holder.binding.textCategory.text=data.category

    }

    override fun getItemCount(): Int {
       return list.size
    }


}