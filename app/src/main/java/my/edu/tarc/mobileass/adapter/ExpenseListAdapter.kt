package my.edu.tarc.mobileass.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.mobileass.databinding.AllExpenseLayoutBinding
import my.edu.tarc.mobileass.model.ExpenseViewModel
import my.edu.tarc.mobileass.ui.expense.ExpenseHomeFragmentDirections

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
        holder.binding.texttotalExpense.text="RM"+ data.expense.toString()
        holder.binding.textTitle.text=data.title
        holder.binding.textCategory.text=data.category

        holder.binding.buttonDelete.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Record Permanently?")
            builder.setMessage("Confirm Delete")
            builder.setPositiveButton("Yes") { _, _ ->
                val db = Firebase.firestore
                val storageRef = db.collection("expense").document(list[position].id!!)

                storageRef.delete()
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener { e ->
                        Log.d("MyApp", e.toString())
                    }
            }
            builder.setNegativeButton("No", null)
            val dialog = builder.create()
            dialog.show()
        }



        holder.binding.buttonUpdate.setOnClickListener {
            val action = ExpenseHomeFragmentDirections.actionNavigationExpensesToEditExpenseFragment(data.id!!)
            findNavController(holder.itemView).navigate(action)
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }


}


