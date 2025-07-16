package data

object ExerciseUtils {
    fun getWeightedValue(entry: ExerciseEntry): Int {
        val base = entry.durationOrSets.toIntOrNull() ?:0
        return when (entry.name.lowercase()) {
            "push-ups", "sit-ups" -> base * 1         // Reps
            "walking", "cycling" -> base / 5          // Minutes
            "swimming" -> base / 5                    // Swimming is more intense
            else -> base
        }
    }
}