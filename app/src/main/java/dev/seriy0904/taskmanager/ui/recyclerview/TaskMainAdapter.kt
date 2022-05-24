package dev.seriy0904.taskmanager.ui.recyclerview

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
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
        val taskItemMainLayout: CardView = itemView.findViewById(R.id.task_item_main_layout)
        val taskName: TextView = itemView.findViewById(R.id.task_name)
        val taskDescription: TextView = itemView.findViewById(R.id.task_description)
        val taskTime: TextView = itemView.findViewById(R.id.task_time)
        val weekDays: MaterialDayPicker = itemView.findViewById(R.id.task_weekday)
    }

    fun updateList() {
        CoroutineScope(Dispatchers.IO).launch {
            val newList: ArrayList<Task> = arrayListOf()
            newList.addAll(db.getAll() ?: arrayListOf())
            CoroutineScope(Dispatchers.Main).launch {
                val diffCallback = TaskListDiffUtils()
                diffCallback.setLists(tasksList, newList)
                val diffResult = DiffUtil.calculateDiff(diffCallback)
                Log.d("MyTag", "Old: $tasksList \n New: $newList")
                tasksList.clear()
                tasksList.addAll(newList)
                diffResult.dispatchUpdatesTo(this@TaskMainAdapter)
            }
        }
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
        holder.taskTime.text = holder.itemView.context.getString(
            R.string.task_list_time,
            holderTask.hour,
            holderTask.minute
        )
        holder.weekDays.setSelectedDays(holderTask.weekDays)
        val al = AlertDialog.Builder(holder.taskItemMainLayout.context).setTitle("Удалить \"${holderTask.tittle}\"?")
            .setNegativeButton("Да") { dial, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    db.delete(holderTask)
                    updateList()
                    dial.cancel()
                }
            }.setPositiveButton("Нет") { dial, _ ->
                dial.cancel()
            }
        holder.taskItemMainLayout.setOnLongClickListener {
            al.show()
            true
        }
    }
}

class TaskWeekdayLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }
}