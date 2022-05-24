package dev.seriy0904.taskmanager.ui.recyclerview

import androidx.recyclerview.widget.DiffUtil
import dev.seriy0904.taskmanager.db.Task

class TaskListDiffUtils() : DiffUtil.Callback() {
    private val mOldTaskList: ArrayList<Task> = arrayListOf()
    private val mNewTaskList: ArrayList<Task> = arrayListOf()

    fun setLists(oldTaskList: List<Task>, newTaskList: List<Task>) {
        mOldTaskList.clear()
        mNewTaskList.clear()
        mOldTaskList.addAll(oldTaskList)
        mNewTaskList.addAll(newTaskList)
    }

    override fun getOldListSize() = mOldTaskList.size

    override fun getNewListSize() = mNewTaskList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        (mOldTaskList[oldItemPosition].uid == mNewTaskList[newItemPosition].uid)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        (mOldTaskList[oldItemPosition].hour == mNewTaskList[newItemPosition].hour &&
                mOldTaskList[oldItemPosition].minute == mNewTaskList[newItemPosition].minute)
}