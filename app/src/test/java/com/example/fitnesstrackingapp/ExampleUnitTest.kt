import data.ExerciseEntry
import data.ExerciseUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class ExerciseUtilsTest {

    @Test
    fun testPushUpsWeightedValue() {
        val entry = ExerciseEntry("push-ups", "10", "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(10, result)
    }

    @Test
    fun testWalkingWeightedValue() {
        val entry = ExerciseEntry("walking", "25", "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(5, result)
    }

    @Test
    fun testInvalidDuration() {
        val entry = ExerciseEntry("cycling", "abc", "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(0, result)
    }
}