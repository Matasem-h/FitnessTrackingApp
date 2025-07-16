package data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExerciseDao {

    @Insert
    suspend fun insertExercise(entry: ExerciseEntry)

    @Query("SELECT * FROM exercise_entries WHERE date = :targetDate")
    suspend fun getExercisesByDate(targetDate: String): List<ExerciseEntry>

    @Query("SELECT * FROM exercise_entries")
    suspend fun getAllExercises(): List<ExerciseEntry>

    @Query("DELETE FROM EXERCISE_ENTRIES")
    suspend fun deleteAll()
}