package dev.seriy0904.taskmanager.db

import androidx.room.*
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


@Entity
data class Task(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "tittle") val tittle: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "hour") val hour: Int,
    @ColumnInfo(name = "minute") val minute: Int,
    @ColumnInfo(name = "weekdays") val weekDays: List<MaterialDayPicker.Weekday>
)

@Dao
interface UserDao {

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>?

    @Query("SELECT * FROM task WHERE uid = :id")
    fun getById(id: Long): Task

    @Query("SELECT * FROM task WHERE uid=(SELECT max(uid) FROM task)")
    fun getLast(): Task?

    @Query("DELETE FROM task")
    fun deleteAll()

    @Insert
    fun insert(employee: Task)

    @Update
    fun update(employee: Task)

    @Delete
    fun delete(employee: Task)
}

@Database(entities = [Task::class], version = 1)
@TypeConverters(WeekDaysConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

class WeekDaysConverter {
    private val gson = Gson()
    @TypeConverter
    fun stringToWeekList(data: String?): List<MaterialDayPicker.Weekday> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object:TypeToken<List<MaterialDayPicker.Weekday>>(){}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun weekListToString(someObjects: List<MaterialDayPicker.Weekday>): String? {
        return gson.toJson(someObjects)
    }
}