package xcuitest.api

data class DragRequest(
    val startX: Double,
    val startY: Double,
    val endX: Double,
    val endY: Double,
    val duration: Double,
    val holdDuration: Double,
    val appIds: Set<String>? = null,
)
