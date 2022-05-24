package dev.seriy0904.taskmanager.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.seriy0904.taskmanager.R
import dev.seriy0904.taskmanager.db.AppDatabase
import dev.seriy0904.taskmanager.db.Task
import dev.seriy0904.taskmanager.db.TaskApp
import dev.seriy0904.taskmanager.db.UserDao
import dev.seriy0904.taskmanager.ui.recyclerview.TaskMainAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val listAdapter = TaskMainAdapter()
    private val mainListView: RecyclerView by lazy { findViewById(R.id.main_recycler) }
    private val addTaskButton: FloatingActionButton by lazy { findViewById(R.id.add_task_button) }
    private val taskList: ArrayList<Task> = arrayListOf()
    private val db = TaskApp.instance.database.userDao()
    private val createTask: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            listAdapter.updateList()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainListView.layoutManager = LinearLayoutManager(this)
        mainListView.adapter = listAdapter
        listAdapter.updateList()
        addTaskButton.setOnClickListener { createTask.launch(Intent(this, CreateTaskActivity::class.java)) }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear -> CoroutineScope(Dispatchers.IO).launch {
                db.deleteAll()
                listAdapter.updateList()
            }
        }
        return true
    }
}