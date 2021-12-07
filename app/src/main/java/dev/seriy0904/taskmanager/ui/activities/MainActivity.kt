package dev.seriy0904.taskmanager.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import dev.seriy0904.taskmanager.R
import dev.seriy0904.taskmanager.db.AppDatabase
import dev.seriy0904.taskmanager.db.Task
import dev.seriy0904.taskmanager.db.UserDao
import dev.seriy0904.taskmanager.ui.recyclerview.TaskMainAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val listAdapter = TaskMainAdapter()
    private val mainListView: RecyclerView by lazy { findViewById(R.id.main_recycler) }
    private val addTaskButton: ImageButton by lazy { findViewById(R.id.add_task_button) }
    private val taskList: ArrayList<Task> = arrayListOf()
    private val db: UserDao by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "task_database"
        ).build().userDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainListView.layoutManager = LinearLayoutManager(this)
        mainListView.adapter = listAdapter
        CoroutineScope(Dispatchers.IO).launch {
            val lastIndex = db.getLast()?.uid ?: 0
            db.insert(
                Task(
                    lastIndex + 1,
                    "Test ${(Math.random() * 100).toInt()}",
                    "Description ${(Math.random() * 100).toInt()}",
                    "Time ${(Math.random() * 100).toInt()}"
                )
            )
            getTaskList()
        }
        addTaskButton.setOnClickListener {
            startActivity(Intent(this,CreateTaskActivity::class.java))
        }
    }

    private fun getTaskList() {
        taskList.clear()
        taskList.addAll(db.getAll() ?: arrayListOf())
        CoroutineScope(Dispatchers.Main).launch {
            listAdapter.setList(taskList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear -> CoroutineScope(Dispatchers.IO).launch {
                db.deleteAll()
                getTaskList()
            }
        }
        return true
    }
}