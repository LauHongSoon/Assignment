package my.edu.tarc.mobileass.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.mobileass.databinding.EachTaskBinding
import my.edu.tarc.mobileass.model.Task
import my.edu.tarc.mobileass.ui.home.HomeFragmentDirections

class TaskAdapter(private val list:ArrayList<Task>, val context : Context):
    RecyclerView.Adapter<TaskAdapter.ViewHolder>()  {

    inner class ViewHolder(val binding: EachTaskBinding)
        :RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding=EachTaskBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val task: Task= list[position]
        holder.binding.taskName.text=task.taskName
        holder.binding.category.text=task.taskCategory
        holder.binding.taskStatus.text=task.taskStatus
        holder.binding.dueDate.text=task.dueDate
        holder.binding.deleteTask.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Record Permanently?")
            builder.setMessage("Confirm Delete")
            builder.setPositiveButton("Yes") { _, _ ->
                val db = Firebase.firestore
                val storageRef = db.collection("task").document(list[position].id!!)

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
        holder.binding.editTask.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToEditTaskFragment3(task.id!!)
            Navigation.findNavController(holder.itemView).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}