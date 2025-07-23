import data.ExerciseEntry
import data.ExerciseUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class ExerciseUtilsTest {

    // 3 Unit Tests
    // Test 1: Verifies if a valid strength exercise like push-ups returns an integer number.
    @Test
    fun testPushUpsWeightedValue() {
        val entry = ExerciseEntry(name = "push-ups", durationOrSets = "10", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(10, result)
    }

    // Test 2: Verifies that a cardio exercise like walking is weighted correctly.
    @Test
    fun testWalkingWeightedValue() {
        val entry = ExerciseEntry(name = "walking", durationOrSets = "25", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(5, result) // 25 / 5
    }

    // Test 3: Verifies that invalid input does not crash the app and returns 0.
    @Test
    fun testInvalidDuration() {
        val entry = ExerciseEntry(name = "cycling", durationOrSets = "abc", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(0, result)
    }

    // Test 4: Unknown exercise returns 0
    @Test
    fun testUnknownExerciseType() {
        val entry = ExerciseEntry(name = "skydiving", durationOrSets = "10", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(0, result)
    }
    
}