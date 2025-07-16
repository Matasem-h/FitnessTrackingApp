import data.ExerciseEntry
import data.ExerciseUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class ExerciseUtilsTest {

    // 3 Unit Tests!
    @Test
    fun testPushUpsWeightedValue() {
        val entry = ExerciseEntry(name = "push-ups", durationOrSets = "10", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(10, result)
    }

    @Test
    fun testWalkingWeightedValue() {
        val entry = ExerciseEntry(name = "walking", durationOrSets = "25", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(5, result) // 25 / 5
    }

    @Test
    fun testInvalidDuration() {
        val entry = ExerciseEntry(name = "cycling", durationOrSets = "abc", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(0, result)
    }
}