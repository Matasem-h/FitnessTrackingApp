package data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExerciseEntry::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
}