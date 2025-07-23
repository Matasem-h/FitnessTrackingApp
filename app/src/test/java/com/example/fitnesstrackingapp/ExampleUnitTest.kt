import data.ExerciseEntry
import data.ExerciseUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.threeten.bp.LocalDate

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

    // Test 4: Verifies that an unrecognized exercise like skydiving returns the base value.
    @Test
    fun testUnrecognizedExerciseReturnsBase() {
        val entry = ExerciseEntry(name = "skydiving", durationOrSets = "12", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(12, result) // Falls into default: base = 12
    }

    // Test 5: Verifies that a cardio exercise like cycling is weighted correctly.
    @Test
    fun testCardioDivisibleByFive() {
        val entry = ExerciseEntry(name = "cycling", durationOrSets = "30", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(6, result) // 30 / 5
    }

    // Test 6: Verifies that zero duration input returns 0 for running.
    @Test
    fun testZeroDuration() {
        val entry = ExerciseEntry(name = "running", durationOrSets = "0", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(0, result)
    }

    // Test 7: Verifies that a high-duration cardio exercise is weighted correctly.
    @Test
    fun testHighCardioDuration() {
        val entry = ExerciseEntry(name = "walking", durationOrSets = "120", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(24, result) // 120 / 5
    }

    // Test 8: Verifies that a negative input for sit-ups returns the negative value.
    @Test
    fun testNegativeValue() {
        val entry = ExerciseEntry(name = "Sit-Ups", durationOrSets = "-10", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(-10, result)
    }

    // Test 9: Verifies that a decimal input for running returns 0.
    @Test
    fun testDecimalInputReturnsZero() {
        val entry = ExerciseEntry(name = "running", durationOrSets = "15.5", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(0, result) // Invalid input is parsed as 0
    }

    // Test 10: Verifies that a cardio exercise like swimming is weighted correctly.
    @Test
    fun testSwimmingWeightedValue() {
        val entry = ExerciseEntry(name = "swimming", durationOrSets = "20", date = "2025-07-16")
        val result = ExerciseUtils.getWeightedValue(entry)
        assertEquals(4, result) // 20 / 5
    }
}