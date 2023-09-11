package my.edu.tarc.mobileass.ui.expense

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import my.edu.tarc.mobileass.databinding.AllExpenseLayoutBinding

class ExpenseListAdapter(val context: Context, private var list:ArrayList<ExpenseViewModel>)
    :RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder>(){

    inner class ExpenseViewHolder(val binding:AllExpenseLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = AllExpenseLayoutBinding.inflate(LayoutInflater.from(context),parent ,false)
        return  ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val preferences: SharedPreferences= context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val currentUser = preferences.getString("email", "")!!
        val data = list[position]

        holder.binding.textDate.text = data.date
        holder.binding.textCategory.text = data.category
        holder.binding.texttotalExpense.text = "RM" + data.expense
        holder.binding.textTitle.text = data.title

//        holder.itemView.setOnClickListener {
//            if(list[position].user == currentUser){
//                Toast.makeText(context,"This is your own listing", Toast.LENGTH_SHORT).show()
//                val action = AllProductFragmentDirections.actionAllProductFragmentToEditListingFragment(list[position].productId!!)
//                Navigation.findNavController(holder.itemView).navigate(action)
//            }
//            else{
//                val action =
//                    AllProductFragmentDirections.actionAllProductFragmentToProductDetailsFragment(
//                        data.productCategory!!,
//                        list[position].productId!!
//                    )
//                Navigation.findNavController(holder.itemView).navigate(action)
//            }
//
//        }

//        holder.binding.btnPrice.setOnClickListener {
//            if(list[position].userEmail == currentUser){
//                Toast.makeText(context,"This is your own listing", Toast.LENGTH_SHORT).show()
//                val action = AllProductFragmentDirections.actionAllProductFragmentToEditListingFragment(list[position].productId!!)
//                Navigation.findNavController(holder.itemView).navigate(action)
//            }
//            else {
//                val action =
//                    AllProductFragmentDirections.actionAllProductFragmentToProductDetailsFragment(
//                        data.productCategory!!,
//                        list[position].productId!!
//                    )
//                Navigation.findNavController(holder.itemView).navigate(action)
//            }
//        }
//        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
//    fun searchDataList(searchList: ArrayList<AddProductModel>) {
//        list = searchList
//        notifyDataSetChanged()
//    }

}