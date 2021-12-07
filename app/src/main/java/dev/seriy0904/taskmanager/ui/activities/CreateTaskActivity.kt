package dev.seriy0904.taskmanager.ui.activities

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import dev.seriy0904.taskmanager.R

class CreateTaskActivity : AppCompatActivity() {
    private val timePicker: TimePicker by lazy { findViewById(R.id.time_picker) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        timePicker.setIs24HourView(true)
    }
}