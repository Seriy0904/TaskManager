package dev.seriy0904.taskmanager.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.seriy0904.taskmanager.R
import dev.seriy0904.taskmanager.db.Task

class TaskMainAdapter : RecyclerView.Adapter<TaskMainAdapter.TaskViewHolder>() {
    private val tasksList: ArrayList<Task> = arrayListOf()

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName:TextView = itemView.findViewById(R.id.task_name)
        val taskDescription:TextView = itemView.findViewById(R.id.task_description)
        val taskTime:TextView = itemView.findViewById(R.id.task_time)
    }

    fun setList(newList:List<Task>) {
        tasksList.clear()
        tasksList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_main_list_item, parent,false)
        )
    }

    override fun getItemCount() = tasksList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val holderTask = tasksList[position]
        holder.taskName.text = holderTask.tittle
        holder.taskDescription.text = holderTask.description
        holder.taskTime.text = holderTask.time
    }
}