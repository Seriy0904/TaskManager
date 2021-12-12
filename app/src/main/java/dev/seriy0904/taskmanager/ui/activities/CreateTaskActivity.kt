package dev.seriy0904.taskmanager.ui.activities


import android.os.Bundle
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.seriy0904.taskmanager.R
import dev.seriy0904.taskmanager.db.AppDatabase
import dev.seriy0904.taskmanager.db.Task
import dev.seriy0904.taskmanager.db.TaskApp
import dev.seriy0904.taskmanager.db.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateTaskActivity : AppCompatActivity() {
    private val db = TaskApp.instance.database.userDao()
    private val timePicker: TimePicker by lazy { findViewById(R.id.time_picker) }
    private val weekPicker: MaterialDayPicker by lazy { findViewById(R.id.week_picker) }
    private val taskTittle: EditText by lazy { findViewById(R.id.task_tittle_add_task) }
    private val taskDescription: EditText by lazy { findViewById(R.id.task_description_add_task) }
    private val saveTaskButton: FloatingActionButton by lazy { findViewById(R.id.save_task_button) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        timePicker.setIs24HourView(true)
        saveTaskButton.setOnClickListener {
            if (taskTittle.text.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val lastIndex = db.getLast()?.uid ?: 0
                    db.insert(
                        Task(
                            lastIndex + 1,
                            taskTittle.text.toString(),
                            taskDescription.text.toString(),
                            timePicker.hour,
                            timePicker.minute,
                            weekPicker.selectedDays
                        )
                    )
                    setResult(0)
                    finish()
                }
            }
        }
    }
}