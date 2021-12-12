package dev.seriy0904.taskmanager.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.antonious.materialdaypicker.MaterialDayPicker
import dev.seriy0904.taskmanager.R
import dev.seriy0904.taskmanager.db.Task
import dev.seriy0904.taskmanager.db.TaskApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskMainAdapter : RecyclerView.Adapter<TaskMainAdapter.TaskViewHolder>() {
    private val tasksList: ArrayList<Task> = arrayListOf()
    private val db = TaskApp.instance.database.userDao()

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.task_name)
        val taskDescription: TextView = itemView.findViewById(R.id.task_description)
        val taskTime: TextView = itemView.findViewById(R.id.task_time)
        val weekDays: MaterialDayPicker = itemView.findViewById(R.id.task_weekday)
    }

    fun setList(newList: List<Task>) {
        tasksList.clear()
        tasksList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_main_list_item, parent, false)
        )
    }

    override fun getItemCount() = tasksList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val holderTask = tasksList[position]
        holder.taskName.text = holderTask.tittle
        holder.taskDescription.text = holderTask.description
        holder.taskTime.text = holder.itemView.context.getString(R.string.task_list_time,holderTask.hour,holderTask.minute)
        holder.weekDays.setSelectedDays(holderTask.weekDays)
        holder.weekDays.setDaySelectionChangedListener { newWeekDays ->
            CoroutineScope(Dispatchers.IO).launch {
                db.update(
                    Task(
                        holderTask.uid,
                        holderTask.tittle,
                        holderTask.description,
                        holderTask.hour,
                        holderTask.minute,
                        newWeekDays
                    )
                )
            }
        }
    }
}