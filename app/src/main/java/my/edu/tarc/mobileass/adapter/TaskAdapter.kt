package my.edu.tarc.mobileass.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import my.edu.tarc.mobileass.databinding.EachTaskBinding
import my.edu.tarc.mobileass.model.Task

class TaskAdapter(private val list:ArrayList<Task>, val context : Context):
    RecyclerView.Adapter<TaskAdapter.ViewHolder>()  {

    inner class ViewHolder(private val binding:EachTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            // Bind data to views in the layout using ViewBinding
            binding.taskName.text = task.taskName
            binding.dueDate.text = task.dueDate
            binding.category.text = task.taskCategory
            binding.taskStatus.text = task.taskStatus
            // Bind other views as needed
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EachTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = list[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}