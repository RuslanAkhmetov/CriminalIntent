import java.util.Date
import java.util.UUID

data class Crime(
    val id: UUID = java.util.UUID.randomUUID(),
    var title: String = "",
    var date: Date = java.util.Date(),
    var isSolved: Boolean = false
)