package dev.seriy0904.taskmanager.db

import androidx.room.*
import androidx.room.Delete

import androidx.room.Update


@Entity
data class Task(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "tittle") val tittle: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "time") val time: String?
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
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}