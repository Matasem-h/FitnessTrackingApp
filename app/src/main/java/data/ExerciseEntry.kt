package data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_entries")
data class ExerciseEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val durationOrSets: String,
    val date: String // Use "yyyy-MM-dd" format
)