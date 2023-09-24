package my.edu.tarc.mobileass.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import my.edu.tarc.mobileass.databinding.EachTaskBinding
import my.edu.tarc.mobileass.model.Task

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
    }

    override fun getItemCount(): Int {
        return list.size
    }
}