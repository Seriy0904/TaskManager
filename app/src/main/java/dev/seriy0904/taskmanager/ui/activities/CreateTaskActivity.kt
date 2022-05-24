package dev.seriy0904.taskmanager.ui.activities


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.seriy0904.taskmanager.R
import dev.seriy0904.taskmanager.db.Task
import dev.seriy0904.taskmanager.db.TaskApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CreateTaskActivity : AppCompatActivity() {
    private val db = TaskApp.instance.database.userDao()
    private val timePicker: TimePicker by lazy { findViewById(R.id.time_picker) }
    private val weekPicker: MaterialDayPicker by lazy { findViewById(R.id.week_picker) }
    private val taskTittle: EditText by lazy { findViewById(R.id.task_tittle_add_task) }
    private val taskDescription: EditText by lazy { findViewById(R.id.task_description_add_task) }
    private val saveTaskButton: FloatingActionButton by lazy { findViewById(R.id.save_task_button) }
    private val calendarPicker: CalendarView by lazy { findViewById(R.id.calendar_picker) }
    private val disposableTask: CheckBox by lazy { findViewById(R.id.disposableTask) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        timePicker.setIs24HourView(true)
        calendarPicker.visibility = View.GONE
        disposableTask.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                calendarPicker.visibility = View.VISIBLE
                timePicker.visibility = View.GONE
                weekPicker.visibility = View.GONE
            }else{
                calendarPicker.visibility = View.GONE
                timePicker.visibility = View.VISIBLE
                weekPicker.visibility = View.VISIBLE
            }
        }
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
                    val nextDay = Calendar.getInstance()
                    for (i in weekPicker.selectedDays.sorted()) {
                        val ordinal = i.ordinal + 1
                        if (ordinal > nextDay.get(Calendar.DAY_OF_WEEK)) {
                            val dif = ordinal - nextDay.get(Calendar.DAY_OF_WEEK)
                            nextDay.set(
                                Calendar.DAY_OF_YEAR,
                                nextDay.get(Calendar.DAY_OF_YEAR) + dif
                            )
                            break
                        }
                    }
                    Log.d("MyTag", "Day: ${nextDay.get(Calendar.DAY_OF_MONTH)}")
                    nextDay.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                    nextDay.set(Calendar.MINUTE, timePicker.minute)
                    Log.d(
                        "MyTag",
                        "Hour: ${nextDay.get(Calendar.HOUR_OF_DAY)}, Picker: ${timePicker.hour}"
                    )
                    if (Calendar.getInstance().timeInMillis >= nextDay.timeInMillis) {
                        Log.d("MyTag", "Time is up")
                        nextDay.set(Calendar.DAY_OF_YEAR, nextDay.get(Calendar.DAY_OF_YEAR) + 7)
                    }
//                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                    val alarmInfo = AlarmManager.AlarmClockInfo()
                    setResult(0)
                    finish()
                }
            } else {
                Toast.makeText(this, "Заголовок задания пустой", Toast.LENGTH_SHORT).show()
            }
        }
    }
}